package io.github.isaac.vulcano.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    @GetMapping
    public ResponseEntity<String> listar() {
        return ResponseEntity.ok("Listar todos los inventarios");
    }

    // Recurso y jugador forman la clave compuesta: /api/inventarios/{jugadorId}/{recursoId}
    @GetMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> obtenerPorId(@PathVariable Integer jugadorId, @PathVariable Integer recursoId) {
        return ResponseEntity.ok("Obtener inventario por jugadorId: " + jugadorId + " y recursoId: " + recursoId);
    }



    @PostMapping
    public ResponseEntity<String> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Crear nuevo inventario");
    }

    @PutMapping("/{jugadorId}/{recursoId}")
    public ResponseEntity<String> actualizar(@PathVariable Integer jugadorId, @PathVariable Integer recursoId) {
        return ResponseEntity.ok("Actualizar inventario para jugadorId: " + jugadorId + " y recursoId: " + recursoId);
    }

    @DeleteMapping("/{jugadorId}/{recursoId}")
    public ResponseEntity<String> eliminar(@PathVariable Integer jugadorId, @PathVariable Integer recursoId) {
        return ResponseEntity.ok("Eliminar inventario para jugadorId: " + jugadorId + " y recursoId: " + recursoId);
    }
}

