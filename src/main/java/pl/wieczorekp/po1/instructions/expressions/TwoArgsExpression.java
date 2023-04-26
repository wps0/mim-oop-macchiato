package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public abstract class TwoArgsExpression extends Expression {
    // left and right operands
    protected Expression lop;
    protected Expression rop;

    protected TwoArgsExpression(CodeBlock context, Expression lop, Expression rop) {
        super(context);
        this.lop = lop;
        this.rop = rop;
    }
}
