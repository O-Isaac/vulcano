package io.github.isaac.vulcano.entities.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseListEntity<T> extends ResponseEntity<List<T>> {

    public ResponseListEntity(List<T> body, HttpStatus status) {
        super(body, status);
    }

    // Constructor de conveniencia para éxito 200 OK
    public static <T> ResponseListEntity<T> ok(List<T> list) {
        return new ResponseListEntity<>(list, HttpStatus.OK);
    }
}