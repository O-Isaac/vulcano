package io.github.isaac.vulcano.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/objetos")
public class ObjetoController {

    @GetMapping
    public ResponseEntity<String> listar() {
        return ResponseEntity.ok("Listar todos los objetos");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok("Obtener objeto por id: " + id);
    }

    @PostMapping
    public ResponseEntity<String> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Crear nuevo objeto");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar objeto con id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok("Eliminar objeto con id: " + id);
    }
}

