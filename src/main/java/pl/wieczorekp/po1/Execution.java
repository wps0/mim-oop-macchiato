package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

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
                            Execution error at statement %s
                            Exception: %s
                            Cause: %s
                            Program state:
                            %s
                            """,
                    cause.isPresent() ? cause.get() : "unknown",
                    e.getClass().getName(),
                    e.getMessage(),
                    cause.isEmpty() ? "unknown" : code.toString());
            e.printStackTrace();
        } finally {
            System.out.println("Program finished");
        }
    }

    protected void execute() {
        code.execute();
    }
}
