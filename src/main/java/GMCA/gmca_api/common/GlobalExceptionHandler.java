package GMCA.gmca_api.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(IllegalArgumentException ex) { return ResponseEntity.badRequest().body(new ApiError(ex.getMessage())); }
    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ApiError> conflict(IllegalStateException ex) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(ex.getMessage())); }
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    ResponseEntity<ApiError> validation(Exception ex) { return ResponseEntity.badRequest().body(new ApiError("Datos invalidos")); }
}
