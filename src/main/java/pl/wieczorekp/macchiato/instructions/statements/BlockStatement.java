package pl.wieczorekp.macchiato.instructions.statements;

import java.util.Optional;

public abstract class BlockStatement extends Statement {
    protected BlockStatement(CodeBlock context) {
        super(context);
    }

    public abstract Optional<Statement> getCurrentStatement(boolean shiftIP);
    public abstract boolean hasEnded();

    public void execute() {
        while (!hasEnded()) {
            executeOne();
        }
    }

    public void setContext(CodeBlock context) {
        this.context = context;
    }
}
