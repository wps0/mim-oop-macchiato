package pl.wieczorekp.po1.instructions;

public class ExecutionEndedException extends IllegalStateException {
    public ExecutionEndedException() {
    }

    public ExecutionEndedException(String s) {
        super(s);
    }
}
