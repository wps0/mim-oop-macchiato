package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.statements.BlockStatement;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebuggerExecution extends Execution {
    public DebuggerExecution(CodeBlock code) {
        super(code);
    }

    public void continueExecution() {
        super.execute();
    }

    public void step(int steps) {
        for (int i = 0; i < steps; i++) {
            try {
                code.executeOne();
                System.out.println(code.getCurrentStatement());
            } catch (ExecutionEndedException e) {
                System.out.println("Program finished");
            }
        }
    }

    public void display(int maxScope) {
        Optional<Statement> currentStatement = code.getCurrentStatement();
        if (currentStatement.isEmpty()) {
            throw new NoSuchElementException("no statement is being executed");
        }

        Map<String, Integer> vars = currentStatement.get().getContext().getVariables();
        CodeBlock block = currentStatement.get().getContext();
        while (maxScope >= 0 && block != null) {
            block.getVariables().forEach(vars::putIfAbsent);

            maxScope--;
            block = block.getContext();
        }

        if (maxScope >= 0 && block == null) {
            System.out.println("The instruction is not nested that deep");
        }
        System.out.println("Variables:");
        vars.forEach((var, val) -> System.out.printf("%-16s: %d", var, val));
    }

    public void exit() {
        System.out.println("Program interrupted");
    }

    @Override
    public void execute() {
        System.out.println(" -------------------------------------- ");
        System.out.printf("%29s\n", "MACCHIATO DEBUGGER");
        System.out.println(" -------------------------------------- ");

        // matches strings formatted like: <str> <int>
        Pattern inputCmdPattern = Pattern.compile("([A-Za-z]+)\\s+(\\d*)");
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                Matcher lineMatcher = inputCmdPattern.matcher(line);

                if (!lineMatcher.find()) {
                    System.err.println("Invalid command " + line.stripLeading().stripTrailing());
                    continue;
                }
                String cmd = lineMatcher.group(1);
                Integer arg = lineMatcher.groupCount() == 2 ? Integer.valueOf(lineMatcher.group(2)) : null;

                switch (cmd) {
                    case "c", "continue" -> continueExecution();
                    case "e", "exit" -> exit();
                }

                if (arg != null) {
                    switch (cmd) {
                        case "s", "step" -> step(arg);
                        case "d", "display" -> display(arg);
                        default -> System.err.printf("Command %s not found", cmd);
                    }
                }
            }
        }
    }
}
