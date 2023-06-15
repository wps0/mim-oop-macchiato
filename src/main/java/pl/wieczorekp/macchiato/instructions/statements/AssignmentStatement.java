package pl.wieczorekp.macchiato.instructions.statements;

import pl.wieczorekp.macchiato.instructions.expressions.Expression;

public class AssignmentStatement extends Statement {
    protected String varName;
    protected Expression value;

    public AssignmentStatement(CodeBlock context, String varName, Expression value) {
        super(context);
        this.varName = varName;
        this.value = value;
    }

    @Override
    public void executeOne() {
        context.assignVariable(varName, value.evaluateInContext(context));
    }

    @Override
    public String toString() {
        return varName + " := " + value;
    }
}
