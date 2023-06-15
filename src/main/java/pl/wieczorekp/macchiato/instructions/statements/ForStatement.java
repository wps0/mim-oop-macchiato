package pl.wieczorekp.macchiato.instructions.statements;

import pl.wieczorekp.macchiato.instructions.expressions.Expression;

import java.util.Optional;

public class ForStatement extends BlockStatement {
    private final String controlVariableName;
    private final CodeBlock body;
    private final Expression repsExpression;
    private State loopState;
    private int counter;
    private int reps;

    public ForStatement(CodeBlock context, String controlVariableName, Expression reps, CodeBlock body) {
        super(context);
        this.counter = 0;
        this.controlVariableName = controlVariableName;
        this.repsExpression = reps;
        this.reps = 0;
        this.body = body;
        loopState = State.STARTING;
    }

    private void updateState() {
        if (loopState != State.FINISHING && (hasEnded() || body.hasEnded())) {
            loopState = State.FINISHING;
        } else if (loopState == State.STARTING) {
            loopState = State.EXECUTING;
        } else if (loopState == State.FINISHING) {
            loopState = State.STARTING;
        }
    }

    private void beginNewIteration() {
        body.resetVariables();
        body.declareVariable(controlVariableName, counter);
        body.setInstructionPointer(0);
    }

    @Override
    public void executeOne() {
        if (loopState == State.STARTING) {
            if (hasEnded()) {
                // the first execution in this context
                reps = repsExpression.evaluateInContext(context);
                counter = 0;
            }
            // a next iteration of the loop
            beginNewIteration();
        } else if (loopState == State.EXECUTING) {
            body.executeOne();
        } else if (loopState == State.FINISHING) {
            counter++;
        } else {
            throw new IllegalStateException();
        }

        updateState();
    }

    @Override
    public boolean hasEnded() {
        return counter >= reps;
    }

    @Override
    public Optional<Statement> getCurrentStatement(boolean shiftIP) {
        if (loopState != State.EXECUTING) {
            return Optional.of(this);
        }
        return body.getCurrentStatement(shiftIP);
    }

    @Override
    public String toString() {
        return "for " + controlVariableName + " := 0 (current value: " +
                counter + ')' + " < " + reps +
                " (reps expression: " + repsExpression + "; hasEnded=" + hasEnded() + ")\n" +
                body.toString();
    }

    private enum State {
        STARTING, EXECUTING, FINISHING
    }
}
