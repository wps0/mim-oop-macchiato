package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DebuggerExecution extends Execution {
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
                return false;
            }

            try {
                Integer.valueOf(args.get(0));
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        };
        ONE_PATH_ARG_VERIFICATION_STRATEGY = args -> {
            if (args.size() != 1) {
                return false;
            }

            try {
                File f = new File(args.get(0));
                if (!f.exists() || !f.isFile() || !f.canRead()) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }

            return true;
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
                "display <max distance>",
                "display variables accessible by the current instruction",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> help()
        ));
        commands.add(new Command("s",
                "step <steps>",
                "execute <steps> instructions",
                ONE_INT_ARG_VERIFICATION_STRATEGY,
                args -> step(Integer.parseInt(args.get(0)))));
        commands.add(new Command("s",
                "step",
                "execute 1 instruction",
                NO_ARGS_VERIFICATION_STRATEGY,
                args -> step(1)));
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
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                Matcher lineMatcher = inputCmdPattern.matcher(line);

                if (!lineMatcher.find()) {
                    System.err.println("Invalid command " + line.stripLeading().stripTrailing());
                    continue;
                }
                String cmd = lineMatcher.group(1);
                List<String> args = new ArrayList<>();
                if (lineMatcher.group(2) != null) {
                    args = List.of(lineMatcher.group(2));
                }

                List<Command> matchingCommands = findMatchingCommands(cmd);
                if (matchingCommands.isEmpty()) {
                    System.err.println("Invalid command " + cmd);
                    continue;
                }
                executeCommand(matchingCommands, args);
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
