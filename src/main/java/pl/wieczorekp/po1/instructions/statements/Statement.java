package pl.wieczorekp.po1.instructions.statements;

public abstract class Statement {
    protected CodeBlock context;
    protected Statement(CodeBlock context) {
        this.context = context;
    }

    public void execute() {
        while (!hasEnded()) {
            executeOne();
        }
    }

    public abstract void executeOne();
    protected abstract boolean hasEnded();

    public CodeBlock getContext() {
        return context;
    }
}
