package pilipala.userdocumentation.exceptions;

public class DesafioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DesafioException(String message) {
        super(message);
    }

    public DesafioException(String message, Throwable cause) {
        super(message, cause);
    }

}