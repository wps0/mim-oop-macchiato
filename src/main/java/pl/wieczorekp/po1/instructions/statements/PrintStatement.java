package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.expressions.Expression;

public class PrintStatement extends Statement {
    private Expression message;
    private boolean hasEnded;

    public PrintStatement(CodeBlock context, Expression message) {
        super(context);
        this.message = message;
        this.hasEnded = false;
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException();
        }

        System.out.println(message.evaluateInContext(context));
        hasEnded = true;
    }

    @Override
    protected boolean hasEnded() {
        return hasEnded;
    }

    @Override
    public String toString() {
        return "print " + message;
    }
}
