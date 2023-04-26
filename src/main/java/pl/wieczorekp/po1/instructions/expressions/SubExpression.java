package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class SubExpression extends TwoArgsExpression {
    protected SubExpression(CodeBlock context, Expression lop, Expression rop) {
        super(context, lop, rop);
    }

    @Override
    public Integer evaluate() {
        return lop.evaluateInContext(context) - rop.evaluateInContext(context);
    }
}
