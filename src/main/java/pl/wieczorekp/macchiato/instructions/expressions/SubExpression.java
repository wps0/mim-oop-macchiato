package pl.wieczorekp.macchiato.instructions.expressions;

import pl.wieczorekp.macchiato.instructions.statements.CodeBlock;

public class SubExpression extends TwoArgsExpression {
    public SubExpression(Expression lop, Expression rop) {
        super(lop, rop);
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return lop.evaluateInContext(context) - rop.evaluateInContext(context);
    }

    @Override
    public String toString() {
        return lop + " - " + rop;
    }

    public static SubExpression of(Expression lop, Expression rop) {
        return new SubExpression(lop, rop);
    }
}
