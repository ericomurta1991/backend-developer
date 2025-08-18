package pilipala.userdocumentation.exceptions;

import org.springframework.http.HttpStatus;

public class DesafioException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final HttpStatus status;

    // Construtor "principal"
    public DesafioException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
    //Construtor com mensagem + status
    public DesafioException(String message, HttpStatus status) {
        this(message, null, status);
    }

    // Construtor com apenas mensagem (status padrão BAD_REQUEST)
    public DesafioException(String message) {
        this(message, null, HttpStatus.BAD_REQUEST);
    }

    // Construtor com mensagem + causa (status padrão BAD_REQUEST)
    public DesafioException(String message, Throwable cause) {
        this(message, cause, HttpStatus.BAD_REQUEST);
    }

    public HttpStatus getStatus() {
        return status;
    }


}