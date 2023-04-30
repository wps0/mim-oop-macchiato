package pl.wieczorekp.po1.instructions.statements;

import pl.wieczorekp.po1.instructions.expressions.Expression;

import java.util.Optional;

public class ForStatement extends BlockStatement {
    private final String controlVariableName;
    private final CodeBlock body;
    private Expression repsExpression;
    private int counter;
    private int reps;

    public ForStatement(CodeBlock context, String controlVariableName, Expression reps, CodeBlock body) {
        super(context);
        this.counter = 0;
        this.controlVariableName = controlVariableName;
        this.repsExpression = reps;
        this.reps = 0;
        this.body = body;
    }

    private void setup() {
        if (repsExpression != null) {
            reps = repsExpression.evaluateInContext(context);
            repsExpression = null;
        }
        body.assignVariable(controlVariableName, counter);
    }

    @Override
    public void executeOne() {
        if (repsExpression != null || body.hasEnded()) {
            // first-time execution in the current context
            setup();
        }
        if (hasEnded()) {
            return;
        }

        if (body.hasEnded()) {
            body.setInstructionPointer(0);
            body.resetVariables();
            counter++;
            setup();
        } else {
            body.executeOne();
        }
    }

    @Override
    public boolean hasEnded() {
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
        return "for " + controlVariableName + " := 0 (current value: " +
                counter + ')' + " < " + reps +
                " (reps expression: " + repsExpression + "; hasEnded=" + hasEnded() + ")\n" +
                body.toString();
    }
}
