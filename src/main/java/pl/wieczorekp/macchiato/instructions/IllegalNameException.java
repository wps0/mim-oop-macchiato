package pl.wieczorekp.macchiato.instructions;

public class IllegalNameException extends RuntimeException {
    public IllegalNameException() {
    }

    public IllegalNameException(String message) {
        super(message);
    }
}
