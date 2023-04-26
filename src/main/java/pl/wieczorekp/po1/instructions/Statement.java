package pl.wieczorekp.po1.instructions;

public abstract class Statement extends Instruction {
    protected Statement(CodeBlock containingBlock) {
        super(containingBlock);
    }

    public void execute() {
        while (!hasEnded()) {
            executeOne();
        }
    }

    public abstract void executeOne();
    protected abstract boolean hasEnded();
}
