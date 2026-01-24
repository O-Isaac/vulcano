package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.ComponenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ComponenteController {

    private final ComponenteService componenteService;

    // --- RUTAS JERÁRQUICAS (Basadas en el Plano) ---

    @GetMapping("/planos/{planoId}/componentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<ComponenteResponse> listarPorPlano(@PathVariable Integer planoId) {
        return ResponseListEntity.ok(componenteService.listarPorPlano(planoId));
    }

    @PostMapping("/planos/{planoId}/componentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> agregarAlPlano(
            @PathVariable Integer planoId,
            @Valid @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteService.crearParaPlano(planoId, request));
    }

    @PostMapping("/planos/{planoId}/componentes/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<ComponenteResponse> agregarAlPlanoBulk(@PathVariable Integer planoId,
        @Valid @RequestBody List<ComponenteCreateRequest> request) {
        return ResponseListEntity.created(componenteService.crearParaPlanoBulk(planoId, request));
    }

    // --- RUTAS INDIVIDUALES (Basadas en el ID del Componente) ---

    @GetMapping("/componentes")
    public ResponseListEntity<ComponenteResponse> listar() {
        return ResponseListEntity.ok(
                componenteService.listar()
        );
    }

    @GetMapping("/componentes/{id}")
    public ResponseEntity<ComponenteResponse> obtenerPorId(@PathVariable Integer id) {
        return componenteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/componentes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.ok(componenteService.actualizar(id, request));
    }

    @DeleteMapping("/componentes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        componenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}