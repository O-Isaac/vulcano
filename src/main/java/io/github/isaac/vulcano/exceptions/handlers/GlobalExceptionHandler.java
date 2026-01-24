package io.github.isaac.vulcano.exceptions.handlers;

import io.github.isaac.vulcano.exceptions.BadRequestException;
import io.github.isaac.vulcano.exceptions.TokenException;
import io.github.isaac.vulcano.exceptions.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Error de formato (415) - Por ejemplo, no enviar JSON
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String supportedTypes = ex.getSupportedMediaTypes().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of(
                        "error", "Formato de contenido no soportado",
                        "mensaje", "Tu petición debe usar: " + supportedTypes
                ));
    }

    // 2. Errores de lógica de negocio (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // 3. Conflictos de datos (409)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    // 4. Errores de login (401)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthError(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciales incorrectas o token inválido"));
    }

    // 5. Error genérico (500) para evitar filtrar detalles internos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Ha ocurrido un error inesperado en el servidor"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extraemos cada error de campo y su mensaje
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Validación fallida",
                        "detalles", errors
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Cuerpo de la petición inválido",
                        "mensaje", "El JSON enviado está mal formado o el cuerpo de la petición está vacío"
                ));
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAuthorizationDenied(
            AuthorizationDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "error", "Acceso denegado",
                        "mensaje", "No tienes permisos para acceder a este recurso"
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();
        Map<String, String> errors = new HashMap<>();

        // 1. Caso: Dato demasiado largo (el de tu log)
        if (message.contains("Data too long for column")) {
            // Extraemos el nombre de la columna si es posible, o usamos un mensaje genérico
            String column = extractColumnName(message);
            errors.put(column, "El contenido es demasiado largo para este campo");
        }
        // 2. Caso: Duplicados (Unique constraints)
        else if (message.contains("Duplicate entry")) {
            errors.put("error", "Ya existe un registro con estos datos");
        }
        // 3. Caso general de integridad
        else {
            errors.put("database", "Error de integridad de datos");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Validación de base de datos fallida",
                        "detalles", errors
                ));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, String>> handleTokenException(TokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Fallo en la validación del token",
                        "mensaje", ex.getMessage()
                ));
    }



    /**
     * Método auxiliar para limpiar el nombre de la columna del mensaje de error
     */
    private String extractColumnName(String message) {
        try {
            // El mensaje suele ser: Data too long for column 'desc' at row 1
            int start = message.indexOf("'") + 1;
            int end = message.indexOf("'", start);
            return message.substring(start, end);
        } catch (Exception e) {
            return "campo";
        }
    }
}