package mate.academy.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Exception exception) {
        super(message, exception);
    }
}
