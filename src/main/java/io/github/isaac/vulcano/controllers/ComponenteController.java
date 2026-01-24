package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.ComponenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/componentes")
@RequiredArgsConstructor
public class ComponenteController {

    private final ComponenteService componenteService;

    @GetMapping
    public ResponseListEntity<ComponenteResponse> listar() {
        return ResponseListEntity.ok(
                componenteService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> obtenerPorId(@PathVariable Integer id) {
        return componenteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> crear(@Valid @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                componenteService.crear(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> actualizar(@PathVariable Integer id, @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.ok(
                componenteService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        componenteService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}

