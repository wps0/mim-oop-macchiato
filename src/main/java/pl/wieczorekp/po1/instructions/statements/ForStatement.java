package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.ExecutionEndedException;
import pl.wieczorekp.po1.instructions.expressions.Expression;

import java.util.Optional;

public class ForStatement extends BlockStatement {
    private CodeBlock body;
    private String controlVariableName;
    private int counter;
    private int reps;
    private Expression repsExpression;

    // TODO: jak to z tymi zmiennymi ma byc?
    public ForStatement(CodeBlock context, String controlVariableName, Expression reps, CodeBlock body) {
        super(context);
        this.counter = 0;
        this.controlVariableName = controlVariableName;
        this.repsExpression = reps;
        this.reps = 0;
        this.body = body;
    }

    @Override
    public void executeOne() {
        if (counter == 0) {
            reps = repsExpression.evaluateInContext(context);
            body.assignVariable(controlVariableName, counter);
        }
        if (hasEnded()) {
            throw new ExecutionEndedException();
        }

        if (body.hasEnded()) {
            body.setInstructionPointer(0);
            body.assignVariable(controlVariableName, ++counter);
        } else {
            body.executeOne();
        }
    }

    @Override
    protected boolean hasEnded() {
        return counter >= reps;
    }

    @Override
    public Optional<Statement> getCurrentStatement() {
        if (hasEnded()) {
            return Optional.empty();
        }
        Optional<Statement> bodyStatement = body.getCurrentStatement();
        if (bodyStatement.isEmpty()) {
            return Optional.of(this);
        }
        return bodyStatement;
    }

    @Override
    public String toString() {
        StringBuilder asString = new StringBuilder();

        asString.append("for ").append(controlVariableName).append(" := 0 (current value: ")
                .append(counter).append(')').append(" < ").append(reps)
                .append(" (reps expression: ").append(repsExpression).append("; hasEnded=").append(hasEnded()).append(")\n");
        asString.append(body.toString());

        return asString.toString();
    }
}
