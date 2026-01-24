package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.plano.PlanoCreateRequest;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.PlanoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planos")
@RequiredArgsConstructor
public class PlanoController {

    private final PlanoService planoService;

    @GetMapping
    public ResponseListEntity<PlanoResponse> listar() {
        return ResponseListEntity.ok(planoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponse> obtenerPorId(@PathVariable Integer id) {
        return planoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponse> crear(@RequestBody @Valid PlanoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(planoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponse> actualizar(@PathVariable Integer id, @RequestBody PlanoCreateRequest request) {
        return ResponseEntity.ok(planoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        planoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

