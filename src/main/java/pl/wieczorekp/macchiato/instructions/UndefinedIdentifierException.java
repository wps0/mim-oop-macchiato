package pl.wieczorekp.macchiato.instructions;

public class UndefinedIdentifierException extends RuntimeException {
    public UndefinedIdentifierException() {
    }

    public UndefinedIdentifierException(String message) {
        super(message);
    }
}
