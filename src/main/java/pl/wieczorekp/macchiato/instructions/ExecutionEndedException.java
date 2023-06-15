package pl.wieczorekp.macchiato.instructions;

public class ExecutionEndedException extends IllegalStateException {
    public ExecutionEndedException() {
    }

    public ExecutionEndedException(String s) {
        super(s);
    }
}
