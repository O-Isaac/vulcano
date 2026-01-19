package io.github.isaac.vulcano.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    @GetMapping
    public ResponseEntity<String> listar() {
        return ResponseEntity.ok("Listar todos los recursos");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok("Obtener recurso por id: " + id);
    }

    @PostMapping
    public ResponseEntity<String> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Crear nuevo recurso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar recurso con id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok("Eliminar recurso con id: " + id);
    }
}

