package pl.wieczorekp.po1.instructions.statements;

public abstract class Statement {
    protected final CodeBlock context;
    protected Statement(CodeBlock context) {
        this.context = context;
    }

    public abstract void executeOne();

    public CodeBlock getContext() {
        return context;
    }
}
