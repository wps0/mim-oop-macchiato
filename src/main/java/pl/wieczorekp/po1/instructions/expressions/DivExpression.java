package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class DivExpression extends TwoArgsExpression {
    public DivExpression(Expression lop, Expression rop) {
        super(lop, rop);
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return lop.evaluateInContext(context) / rop.evaluateInContext(context);
    }

    @Override
    public String toString() {
        return lop + " / " + rop;
    }
}
