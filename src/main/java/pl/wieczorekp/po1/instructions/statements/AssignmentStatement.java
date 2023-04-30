package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.expressions.Expression;

public class AssignmentStatement extends Statement {
    private String varName;
    private Expression value;

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
        return "var " + varName + " := " + value;
    }
}
