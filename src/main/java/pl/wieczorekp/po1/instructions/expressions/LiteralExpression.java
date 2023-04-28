package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class LiteralExpression implements Expression {
    private Integer literal;

    protected LiteralExpression(Integer literal) {
        this.literal = literal;
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return literal;
    }
}
