package pilipala.userdocumentation.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	 // Trata a exceçao customizada DesafioException (erros de negocio)
    @ExceptionHandler(DesafioException.class)
    public ResponseEntity<StandardError> handleDesafioException(DesafioException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400
        
        StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Bad Request");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // Trata erro 404 para rotas nao encontradas
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<StandardError> handleNotFoundException(org.springframework.web.servlet.NoHandlerFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Not Found");
        err.setMessage("Recurso não encontrado");
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }

    // Trata outras exceçoes genericas (exceçoes inesperadas)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Internal Server Error");
        err.setMessage("Ocorreu um erro inesperado");
        err.setPath(request.getRequestURI());

        // Aqui você pode fazer log do erro para debugging
        ex.printStackTrace();

        return ResponseEntity.status(status).body(err);
    }
}
