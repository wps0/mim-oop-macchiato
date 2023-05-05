package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;

public class Execution {
    protected CodeBlock code;

    public Execution(CodeBlock code) {
        this.code = code;
    }

    public void run() {
        try {
            execute();
        } catch (Exception e) {
            Optional<Statement> cause = code.getCurrentStatement(true);
            System.err.printf("""
                            > Execution error at statement %s
                            > Exception: %s
                            > Cause: %s
                            > Program state:
                            %s
                            """,
                    cause.isPresent() ? cause.get() : "unknown",
                    e.getClass().getName(),
                    e.getMessage(),
                    code.toString().stripTrailing());

            System.err.print("> ");
            displayVariables(cause.get().getContext().getNestedness(), System.err);

            System.err.println("> Stack trace:");
            e.printStackTrace();

            System.exit(1);
        } finally {
            System.out.println("Program finished");
        }
    }

    public void displayVariables(int maxScope, PrintStream output) {
        Optional<Statement> currentStatement = code.getCurrentStatement(false);
        if (currentStatement.isEmpty()) {
            throw new ExecutionEndedException("no statement is being executed");
        }

        Map<String, Integer> vars = currentStatement.get().getContext().getVariables();
        CodeBlock block = currentStatement.get().getContext();
        while (maxScope >= 0 && block != null) {
            block.getVariables().forEach(vars::putIfAbsent);

            maxScope--;
            block = block.getContext();
        }

        if (maxScope >= 0) {
            output.println("The instruction is not nested that deep");
        }
        output.println("Variables:");
        vars.forEach((var, val) -> output.printf("%s = %d\n", var, val));
    }

    protected void execute() {
        code.execute();
    }
}
