package pl.wieczorekp.macchiato.instructions.statements;

import pl.wieczorekp.macchiato.instructions.expressions.Expression;

public class PrintStatement extends Statement {
    private final Expression message;

    public PrintStatement(CodeBlock context, Expression message) {
        super(context);
        this.message = message;
    }

    @Override
    public void executeOne() {
        System.out.println(message.evaluateInContext(context));
    }

    @Override
    public String toString() {
        return "print " + message;
    }
}
