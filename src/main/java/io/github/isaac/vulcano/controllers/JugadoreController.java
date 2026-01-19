package io.github.isaac.vulcano.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jugadores")
public class JugadoreController {

    @GetMapping
    public ResponseEntity<String> listar() {
        return ResponseEntity.ok("Listar todos los jugadores");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok("Obtener jugador por id: " + id);
    }

    @PostMapping
    public ResponseEntity<String> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Crear nuevo jugador");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar jugador con id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok("Eliminar jugador con id: " + id);
    }
}

