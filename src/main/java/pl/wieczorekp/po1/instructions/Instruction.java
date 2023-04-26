package pl.wieczorekp.po1.instructions;

public abstract class Instruction {
    private CodeBlock context;

    protected Instruction(CodeBlock context) {
        this.context = context;
    }

    public CodeBlock getContext() {
        return context;
    }

    public Integer evaluateInContext(CodeBlock context) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
