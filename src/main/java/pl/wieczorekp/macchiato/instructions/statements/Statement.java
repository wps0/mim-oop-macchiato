package pl.wieczorekp.macchiato.instructions.statements;

public abstract class Statement {
    protected CodeBlock context;

    protected Statement(CodeBlock context) {
        this.context = context;
    }

    public abstract void executeOne();

    public CodeBlock getContext() {
        return context;
    }
}
