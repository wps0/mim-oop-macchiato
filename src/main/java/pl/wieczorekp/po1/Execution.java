package pl.wieczorekp.po1;

import pl.wieczorekp.po1.instructions.statements.BlockStatement;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;
import pl.wieczorekp.po1.instructions.statements.Statement;

import java.util.Optional;

public class Execution {
    private CodeBlock code;

    public Execution(CodeBlock code) {
        this.code = code;
    }

    public void execute() {
        try {
            code.execute();
        } catch (Exception e) {
            Optional<Statement> cause = code.getCurrentStatement();
            System.err.printf("Execution error at statement %s\n" +
                            "Cause: %s\n" +
                            "Program state:\n%s\n",
                    cause.isPresent() ? cause.get() : "unknown",
                    e.getMessage(),
                    cause.isEmpty() ? "unknown" : code.toString());
            e.printStackTrace();
        }
    }
}
