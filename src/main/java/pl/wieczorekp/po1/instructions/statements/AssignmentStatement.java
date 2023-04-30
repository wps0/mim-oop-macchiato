package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.expressions.Expression;

public class AssignmentStatement extends Statement {
    private String varName;
    private Expression value;
    private boolean hasEnded;

    public AssignmentStatement(CodeBlock context, String varName, Expression value) {
        super(context);
        this.varName = varName;
        this.value = value;
        this.hasEnded = false;
    }

    @Override
    public void executeOne() {
        context.assignVariable(varName, value.evaluateInContext(context));
        hasEnded = true;
    }

    @Override
    protected boolean hasEnded() {
        return hasEnded;
    }

    @Override
    public String toString() {
        return "var " + varName + " := " + value + " (hasEnded=" + hasEnded + ")";
    }
}
