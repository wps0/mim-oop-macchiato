package pl.wieczorekp.po1.instructions;

public class ForLoop extends Statement {
    private CodeBlock body;

    public ForLoop(CodeBlock containingBlock, CodeBlock body) {
        super(containingBlock);
        this.body = body;
    }





    @Override
    public void executeOne() {
        body.executeOne();
    }

    @Override
    protected boolean hasEnded() {
        return body.hasEnded();
    }
}
