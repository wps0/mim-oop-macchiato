package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebuggerExecution extends Execution {
    private static final BiFunction<String, String, String> HELP_ENTRY_FORMATTER;

    static {
        HELP_ENTRY_FORMATTER = (cmd, msg) -> String.format("  %25s - %s", cmd, msg);
    }

    public DebuggerExecution(CodeBlock code) {
        super(code);
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
        System.out.println(HELP_ENTRY_FORMATTER.apply("f(rame)",
                "display the current instruction and the lines after it"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("d(isplay) <max distance>",
                "display variables accessible by the current instruction"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("s(tep) <steps>",
                "execute <steps> instructions; if step is null, executes one instruction"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("c(ontinue)", "an alias for run"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("r(un)", "resumes the execution"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("h(elp)", "display this message"));
        System.out.println(HELP_ENTRY_FORMATTER.apply("e(xit)", "terminate the program"));
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

        // matches strings formatted like: <str> <int>
        Pattern inputCmdPattern = Pattern.compile("([A-Za-z]+)(?:\\s+(\\d+)|)");
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                Matcher lineMatcher = inputCmdPattern.matcher(line);

                if (!lineMatcher.find()) {
                    System.err.println("Invalid command " + line.stripLeading().stripTrailing());
                    continue;
                }
                String cmd = lineMatcher.group(1);
                Integer arg = lineMatcher.group(2) != null ? Integer.valueOf(lineMatcher.group(2)) : null;

                switch (cmd) {
                    case "c", "continue", "r", "run" -> continueExecution();
                    case "s", "step" -> {
                        if (arg == null) {
                            arg = 1;
                        }
                        executeOneArgCmd(this::step, arg);
                    }
                    case "d", "display" -> executeOneArgCmd((i) -> displayVariables(i, System.out), arg);
                    case "f", "frame" -> frame();
                    case "h", "help" -> help();
                    case "e", "exit" -> exit();
                    default -> {
                        System.err.printf("Invalid command %s\n", cmd);
                        help();
                    }
                }
            }
        }
    }
}
