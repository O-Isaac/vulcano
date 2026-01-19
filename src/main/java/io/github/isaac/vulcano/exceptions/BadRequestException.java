package io.github.isaac.vulcano.exceptions;

// Excepción para errores de lógica de negocio (400 Bad Request)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}