package pl.wieczorekp.po1.instructions.expressions;

import pl.wieczorekp.po1.instructions.IllegalNameException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class VariableExpression implements Expression {
    private String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return context.lookupVariable(variableName)
                .orElseThrow(() -> new IllegalNameException("no variable named " + variableName));
    }
}
