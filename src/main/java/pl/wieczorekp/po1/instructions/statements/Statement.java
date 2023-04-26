package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.Instruction;

public abstract class Statement extends Instruction {
    protected Statement(CodeBlock context) {
        super(context);
    }

    public void execute() {
        while (!hasEnded()) {
            executeOne();
        }
    }

    public abstract void executeOne();
    protected abstract boolean hasEnded();
}
