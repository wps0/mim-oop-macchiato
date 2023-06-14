package pl.wieczorekp.po1.instructions;

public class UndefinedIdentifierException extends RuntimeException {
    public UndefinedIdentifierException() {
    }

    public UndefinedIdentifierException(String message) {
        super(message);
    }
}
