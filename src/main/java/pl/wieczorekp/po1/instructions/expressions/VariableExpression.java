package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.UndefinedIdentifierException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public record VariableExpression(String name) implements Expression {
    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return context.lookupVariable(name)
                .orElseThrow(() -> new UndefinedIdentifierException("no variable named " + name));
    }

    @Override
    public String toString() {
        return name;
    }

    public static VariableExpression named(String name) {
        return new VariableExpression(name);
    }
}
