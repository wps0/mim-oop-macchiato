package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.expressions.Expression;
import pl.wieczorekp.po1.instructions.expressions.SimpleExpression;

public class ForStatement extends Statement {
    private CodeBlock body;
    private String controlVariableName;
    private int counter;
    private int reps;

    // TODO: jak to z tymi zmiennymi ma byc?
    protected ForStatement(CodeBlock context, String controlVariableName, Expression reps, CodeBlock body) {
        super(context);
        this.counter = 0;
        this.controlVariableName = controlVariableName;
        this.reps = reps.evaluateInContext(context);
        this.body = body;
    }

    @Override
    public void executeOne() {
        if (hasEnded()) {
            throw new ExecutionEndedException();
        }

        if (body.hasEnded()) {
            body.setInstructionPointer(0);
            body.declareVariable(controlVariableName, ++counter);
        } else {
            body.executeOne();
        }
    }

    @Override
    protected boolean hasEnded() {
        return counter >= reps;
    }
}
