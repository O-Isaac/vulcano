package io.github.isaac.vulcano.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Por defecto devolverá 401
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}