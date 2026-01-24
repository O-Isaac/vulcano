package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponsePrivado;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventarioResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> obtenerPorId(
            @PathVariable Integer jugadorId,
            @PathVariable Integer recursoId) {

        return inventarioService.buscarPorId(jugadorId, recursoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> crear(@PathVariable Integer jugadorId, @PathVariable Integer recursoId) {
        InventarioResponse response = inventarioService.crearInventario(jugadorId, recursoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> actualizar(
            @PathVariable Integer jugadorId,
            @PathVariable Integer recursoId,
            @RequestParam Integer cantidad) {

        InventarioResponse response = inventarioService.actualizarCantidad(jugadorId, recursoId, cantidad);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer jugadorId,
            @PathVariable Integer recursoId) {

        inventarioService.eliminar(jugadorId, recursoId);
        return ResponseEntity.noContent().build();
    }

    // Propio jugador
    @GetMapping("/me")
    public ResponseListEntity<InventarioResponsePrivado> listarPrivado(@AuthenticationPrincipal Jwt jwt) {
        return ResponseListEntity.ok(
                inventarioService.listarPrivado(jwt)
        );
    }

    @PostMapping("/me/{recursoId}")
    public ResponseEntity<InventarioResponsePrivado> crearPrivado(@AuthenticationPrincipal Jwt jwt, @PathVariable Integer recursoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                inventarioService.crearInventarioPrivado(jwt, recursoId)
        );
    }
}