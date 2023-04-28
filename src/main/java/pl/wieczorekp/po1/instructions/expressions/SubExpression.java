package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class SubExpression extends TwoArgsExpression {
    public SubExpression(Expression lop, Expression rop) {
        super(lop, rop);
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return lop.evaluateInContext(context) - rop.evaluateInContext(context);
    }
}
