package pl.wieczorekp.po1.instructions;

public abstract class Expression extends Instruction {
    protected Expression(CodeBlock containingBlock) {
        super(containingBlock);
    }
}
