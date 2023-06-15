package pl.wieczorekp.macchiato.instructions.expressions;

public abstract class TwoArgsExpression implements Expression {
    // left and right operands
    protected Expression lop;
    protected Expression rop;

    protected TwoArgsExpression(Expression lop, Expression rop) {
        this.lop = lop;
        this.rop = rop;
    }
}
