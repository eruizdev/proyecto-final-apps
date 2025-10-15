package main.java.com.coworking.application.coworking_booking.presentation.advice;

import com.coworking.application.coworking_booking.business.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    // Metodo auxiliar para crear el cuerpo del error
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> notFound(NotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("NOT_FOUND", ex.getMessage()));
  }

  // Metodo para manejar errores de validacion y devolver un 400 Bad Request
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> validation(ValidationException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("VALIDATION", ex.getMessage()));
  }

    // Metodo para manejar errores de negocio y devolver un 422 Unprocessable Entity
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> business(BusinessException ex){
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err("BUSINESS", ex.getMessage()));
  }

  // Metodo para manejar errores de autenticacion y devolver un 401 Unauthorized
  private Map<String, Object> err(String code, String msg){
    return Map.of("timestamp", Instant.now().toString(), "code", code, "message", msg);
  }
}