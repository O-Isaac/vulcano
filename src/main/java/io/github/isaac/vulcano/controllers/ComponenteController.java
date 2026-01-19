package io.github.isaac.vulcano.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/componentes")
public class ComponenteController {

    @GetMapping
    public ResponseEntity<String> listar() {
        return ResponseEntity.ok("Listar todos los componentes");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok("Obtener componente por id: " + id);
    }

    @PostMapping
    public ResponseEntity<String> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Crear nuevo componente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar componente con id: " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        return ResponseEntity.ok("Eliminar componente con id: " + id);
    }
}

