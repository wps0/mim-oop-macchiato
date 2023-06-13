package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class LiteralExpression implements Expression {
    public static final LiteralExpression ZERO_LITERAL = new LiteralExpression(0);
    public static final LiteralExpression ONE_LITERAL = new LiteralExpression(1);
    private final Integer literal;

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

    public static LiteralExpression of(Integer literal) {
        return new LiteralExpression(literal);
    }
}
