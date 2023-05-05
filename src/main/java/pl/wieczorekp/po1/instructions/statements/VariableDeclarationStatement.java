package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.expressions.Expression;

public class VariableDeclarationStatement extends AssignmentStatement {
    public VariableDeclarationStatement(CodeBlock context, String varName, Expression value) {
        super(context, varName, value);
    }

    @Override
    public void executeOne() {
        context.declareVariable(varName, value.evaluateInContext(context));
    }

    @Override
    public String toString() {
        return "var " + varName + " := " + value;
    }
}
