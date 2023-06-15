package pl.wieczorekp.macchiato;

import pl.wieczorekp.macchiato.instructions.ExecutionEndedException;
import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;
import pl.wieczorekp.macchiato.instructions.statements.FunctionStatement;
import pl.wieczorekp.macchiato.instructions.statements.Statement;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DebuggerExecution extends Execution {
    private static final int BUFFER_SIZE = 16*1024;
    private static final BiFunction<String, String, String> HELP_ENTRY_FORMATTER;
    private static final Predicate<List<String>> NO_ARGS_VERIFICATION_STRATEGY;
    private static final Predicate<List<String>> ONE_INT_ARG_VERIFICATION_STRATEGY;
    private static final Predicate<List<String>> ONE_PATH_ARG_VERIFICATION_STRATEGY;
    private final List<Command> commands;

    static {
        HELP_ENTRY_FORMATTER = (cmd, msg) -> String.format("  %25s - %s", cmd, msg);
        NO_ARGS_VERIFICATION_STRATEGY = args -> args.size() == 0;
        ONE_INT_ARG_VERIFICATION_STRATEGY = args -> {
            if (args.size() != 1) {
                System.err.println("Wrong number of arguments.");
                return false;
            }

            try {
                Integer.valueOf(args.get(0));
            } catch (NumberFormatException e) {
                System.err.println("Yhe first argument is not an integer.");
                return false;
            }
            return true;
        };
        ONE_PATH_ARG_VERIFICATION_STRATEGY = args -> {
            if (args.size() != 1) {
                System.err.println("Wrong number of arguments.");
                return false;
            }

            Path p = Path.of(args.get(0));
            return Files.notExists(p) || Files.exists(p) && Files.isWritable(p) && Files.isRegularFile(p);
        };
    }

    public DebuggerExecution(CodeBlock code) {
        super(code);
        this.commands = new ArrayList<>();

        commands.add(new Command("c",
                "continue",
                "an alias for run",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> continueExecution()));
        commands.add(new Command("r",
                "run",
                "resumes the execution",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> continueExecution()));
        commands.add(new Command("f",
                "frame",
                "display the current instruction and the lines after it",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> frame()));
        commands.add(new Command(
                "e",
                "exit",
                "terminate the program",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> exit()
        ));
        commands.add(new Command(
                "h",
                "help",
                "display this message",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> help()
        ));
        commands.add(new Command(
                "d",
                "display",
                List.of("max distance"),
                "display variables accessible by the current instruction",
                ONE_INT_ARG_VERIFICATION_STRATEGY,
                args -> displayVariables(Integer.parseInt(args.get(0)), System.out)
        ));
        commands.add(new Command("s",
                "step",
                List.of("steps"),
                "execute <steps> instructions",
                ONE_INT_ARG_VERIFICATION_STRATEGY,
                args -> step(Integer.parseInt(args.get(0)))));
        commands.add(new Command("s",
                "step",
                "execute 1 instruction",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> step(1)));
        commands.add(new Command("m",
                "dump",
                List.of("path"),
                "makes a dump of currently visible variables and procedures and saves it to the given path",
                ONE_PATH_ARG_VERIFICATION_STRATEGY,
                args -> {
                    Path p = Path.of(args.get(0));
                    try (WritableByteChannel chan = Files.newByteChannel(p, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                        dump(chan);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
        commands.sort(null);
    }

    private void dump(WritableByteChannel out) throws IOException {
        ByteBuffer dumpBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        Optional<Statement> curStatement = code.getCurrentStatement(false);

        if (curStatement.isEmpty()) {
            dumpBuffer.put("Unknown current statement".getBytes());
            out.write(dumpBuffer);
        } else {
            dumpBuffer.put("Visible procedures:\n".getBytes());
            Map<String, FunctionStatement> proc = curStatement.get().getContext().getFunctions();

            Iterator<FunctionStatement> it = proc.values().iterator();
            FunctionStatement f = null;
            boolean overflow = false;
            while (it.hasNext() || overflow) {
                if (!overflow) {
                    f = it.next();
                }

                try {
                    dumpBuffer.put(f.getHeader().getBytes());
                    dumpBuffer.putChar('\n');
                    overflow = false;
                } catch (BufferOverflowException e) {
                    overflow = true;
                    dumpBuffer.flip();
                    out.write(dumpBuffer);
                    dumpBuffer.compact();
                }
            }

            if (dumpBuffer.remaining() != 0) {
                dumpBuffer.flip();
                out.write(dumpBuffer);
                dumpBuffer.compact();
            }

            displayVariables(0, new PrintStream(Channels.newOutputStream(out)));
        }
    }

    public void continueExecution() {
        super.execute();
        System.out.println("Program finished");
        System.exit(0);
    }

    public void step(int steps) {
        for (int i = 0; i < steps; i++) {
            try {
                code.executeOne();
            } catch (ExecutionEndedException e) {
                System.out.println("Program finished");
                System.exit(0);
            }
        }
        frame();
    }

    public void frame() {
        Optional<Statement> currentStatement = code.getCurrentStatement(false);
        String frame = currentStatement.isPresent() ? currentStatement.get().toString() : "[unknown]";
        List<String> lines = Arrays.stream(frame.split("\n", 6)).toList();

        System.out.printf("> %s\n", lines.get(0));
        for (int i = 1; i < lines.size()-1; i++) {
            System.out.println(lines.get(i));
        }
        System.out.println("...");
    }

    public void help() {
        System.out.println("Available commands:");
        for (Command cmd : commands) {
            System.out.println(HELP_ENTRY_FORMATTER.apply(cmd.toString(), cmd.description()));
        }
    }

    public void exit() {
        System.out.println("Program interrupted");
        System.exit(0);
    }

    private void executeOneArgCmd(Consumer<Integer> func, Integer arg) {
        if (arg == null) {
            System.err.println("Argument missing");
            return;
        }
        func.accept(arg);
    }

    @Override
    protected void execute() {
        System.out.println(" -------------------------------------- ");
        System.out.printf("%29s\n", "MACCHIATO DEBUGGER");
        System.out.println(" -------------------------------------- ");

        // matches strings formatted like this: <str> [arg1 arg2 ...]
        Pattern inputCmdPattern = Pattern.compile("^(\\S+)( .*)*");
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("# ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher lineMatcher = inputCmdPattern.matcher(line);

                if (!lineMatcher.find()) {
                    System.err.println("Invalid command " + line.stripLeading().stripTrailing());
                    System.out.print("# ");
                    continue;
                }
                String cmd = lineMatcher.group(1);
                List<String> args = new ArrayList<>();
                if (lineMatcher.group(2) != null) {
                    args = List.of(lineMatcher.group(2));
                }
                args = args.stream()
                        .map(s -> s.stripLeading().stripTrailing())
                        .collect(Collectors.toList());

                List<Command> matchingCommands = findMatchingCommands(cmd);
                if (matchingCommands.isEmpty()) {
                    System.err.println("Invalid command " + cmd);
                    System.out.print("# ");
                    continue;
                }

                executeCommand(matchingCommands, args);
                System.out.print("# ");

            }
        }
    }

    private static void executeCommand(List<Command> cmds, List<String> args) {
        Optional<Command> target = cmds.stream()
                .filter(command -> command.verificationStrategy().test(args))
                .findFirst();
        target.ifPresentOrElse(command -> command.executionStrategy().accept(args),
                () -> System.err.println("Invalid argument(s)"));
    }

    private List<Command> findMatchingCommands(String cmd) {
        return commands.stream()
                .filter(command -> command.cmd().equalsIgnoreCase(cmd) || command.shortCmd().equalsIgnoreCase(cmd))
                .collect(Collectors.toList());
    }
}
