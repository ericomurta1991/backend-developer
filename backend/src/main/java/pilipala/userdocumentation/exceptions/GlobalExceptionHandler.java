package pilipala.userdocumentation.exceptions;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Metodo privado para criar StandardError
    private StandardError buildStandardError(HttpStatus status, String message, String path) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError(status.getReasonPhrase());
        err.setMessage(message);
        err.setPath(path);
        return err;
    }

	 // Trata a exceçao customizada DesafioException (erros de negocio)
    @ExceptionHandler(DesafioException.class)
    public ResponseEntity<StandardError> handleDesafioException(DesafioException ex, HttpServletRequest request) {
        HttpStatus status = ex.getStatus() != null ? ex.getStatus() : HttpStatus.BAD_REQUEST;
        StandardError err = buildStandardError(status, ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }


    // Trata erro 404 para rotas nao encontradas
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<StandardError> handleNotFoundException(org.springframework.web.servlet.NoHandlerFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404
        StandardError err = buildStandardError(status, ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // Trata outras exceçoes genericas (exceçoes inesperadas) 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        //log do erro para debugging
        ex.printStackTrace();
        return ResponseEntity.status(status).body(buildStandardError(status, "Ocorreu um erro inesperado", request.getRequestURI()));
    }
    //--------------------------------
    //handler para validação de campos
    //--------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400

        String errorMessages = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)  // mensagem definida na anotação @Size(message = "...") etc
            .collect(Collectors.joining("; "));

        return ResponseEntity.status(status).body(buildStandardError(status, errorMessages, request.getRequestURI()));
    }
    
    //novo tratamento
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandardError> handleMaxSizeException(MaxUploadSizeExceededException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(buildStandardError(status, "arquivo excede o tamanho permitido de 2mb", request.getRequestURI()));
    }
}
