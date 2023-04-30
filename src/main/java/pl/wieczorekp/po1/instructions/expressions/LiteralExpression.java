package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class LiteralExpression implements Expression {
    public static LiteralExpression ZERO_LITERAL = new LiteralExpression(0);
    public static LiteralExpression ONE_LITERAL = new LiteralExpression(1);
    private Integer literal;

    public LiteralExpression(Integer literal) {
        this.literal = literal;
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return literal;
    }

    @Override
    public String toString() {
        return literal.toString();
    }
}
