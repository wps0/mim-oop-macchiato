package pl.wieczorekp.po1.instructions.expressions;

import lombok.Getter;
import pl.wieczorekp.po1.instructions.IllegalNameException;
import pl.wieczorekp.po1.instructions.statements.CodeBlock;

public class VariableExpression implements Expression {
    @Getter
    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Integer evaluateInContext(CodeBlock context) {
        return context.lookupVariable(name)
                .orElseThrow(() -> new IllegalNameException("no variable named " + name));
    }

    @Override
    public String toString() {
        return "var " + name;
    }
}
