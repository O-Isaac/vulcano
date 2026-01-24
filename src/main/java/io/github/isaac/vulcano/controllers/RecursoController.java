package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.recurso.RecursoCreateRequest;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoUpdateRequest;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.RecursoServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
public class RecursoController {

    private final RecursoServices recursoServices;

    @GetMapping
    public ResponseListEntity<RecursoResponse> listar() {
        return ResponseListEntity.ok(
            recursoServices.listarRecurso()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoResponse> obtenerPorId(@PathVariable Integer id) {
        return recursoServices.listarRecursoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecursoResponse> crear(@RequestBody RecursoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                recursoServices.crearRecurso(request)
        );
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<RecursoResponse> crear(@RequestBody List<RecursoCreateRequest> request) {
        return ResponseListEntity.created(recursoServices.crearRecursoBulk(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecursoResponse> actualizar(@Valid @RequestBody RecursoUpdateRequest request, @PathVariable Integer id) {
        return ResponseEntity.ok(
                recursoServices.editarRecurso(request, id)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        recursoServices.eliminarRecurso(id);
        return ResponseEntity.noContent().build();
    }
}

