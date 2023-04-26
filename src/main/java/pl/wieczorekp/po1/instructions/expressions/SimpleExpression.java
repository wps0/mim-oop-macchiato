package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.statements.CodeBlock;

import java.util.Optional;

/**
 * Represents a variable expression or a literal.
 */
public class SimpleExpression extends Expression {
    private Optional<String> variableName;
    private Optional<Integer> instantValue;

    public SimpleExpression(CodeBlock containingBlock, String variableName) {
        super(containingBlock);
        this.variableName = Optional.of(variableName);
        this.instantValue = Optional.empty();
    }

    public SimpleExpression(CodeBlock containingBlock, Integer instantValue) {
        super(containingBlock);
        this.variableName = Optional.empty();
        this.instantValue = Optional.of(instantValue);
    }

    @Override
    public Integer evaluate() {
        return instantValue.orElse(getContext()
                .lookupVariable(variableName.get())
                .get());
    }
}
