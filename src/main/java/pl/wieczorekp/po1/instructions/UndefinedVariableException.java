package pl.wieczorekp.po1.instructions;

public class UndefinedVariableException extends RuntimeException {
    public UndefinedVariableException() {
    }

    public UndefinedVariableException(String message) {
        super(message);
    }
}
