package pl.wieczorekp.po1.instructions;

public class IllegalNameException extends RuntimeException {
    public IllegalNameException() {
    }

    public IllegalNameException(String message) {
        super(message);
    }
}
