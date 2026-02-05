package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.inventario.InventarioResponse;
import io.github.isaac.vulcano.dtos.inventario.InventarioResponsePrivado;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Inventarios", description = "Gestión de inventarios de recursos de los jugadores")
@SecurityRequirement(name = "bearer-jwt")
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(
        summary = "Listar todos los inventarios",
        description = "Obtiene la lista completa de inventarios de todos los jugadores. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InventarioResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @Operation(
        summary = "Obtener inventario específico",
        description = "Retorna el inventario de un jugador para un recurso específico. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> obtenerPorId(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer jugadorId,
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer recursoId) {

        return inventarioService.buscarPorId(jugadorId, recursoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear entrada de inventario",
        description = "Crea una nueva entrada de inventario para un jugador y recurso. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de inventario inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> crear(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer jugadorId,
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer recursoId) {
        InventarioResponse response = inventarioService.crearInventario(jugadorId, recursoId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Actualizar cantidad en inventario",
        description = "Modifica la cantidad de un recurso en el inventario de un jugador. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PutMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventarioResponse> actualizar(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer jugadorId,
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer recursoId,
            @Parameter(description = "Nueva cantidad", required = true)
            @RequestParam Integer cantidad) {

        InventarioResponse response = inventarioService.actualizarCantidad(jugadorId, recursoId, cantidad);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Eliminar entrada de inventario",
        description = "Elimina una entrada de inventario de un jugador. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @DeleteMapping("/{jugadorId}/{recursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer jugadorId,
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer recursoId) {

        inventarioService.eliminar(jugadorId, recursoId);
        return ResponseEntity.noContent().build();
    }

    // Propio jugador
    @Operation(
        summary = "Ver mi inventario",
        description = "Obtiene el inventario completo del jugador autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/me")
    public ResponseListEntity<InventarioResponsePrivado> listarPrivado(@AuthenticationPrincipal Jwt jwt) {
        return ResponseListEntity.ok(
                inventarioService.listarPrivado(jwt)
        );
    }

    @Operation(
        summary = "Crear entrada en mi inventario",
        description = "Crea una nueva entrada de recurso en el inventario del jugador autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entrada de inventario creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping("/me/{recursoId}")
    public ResponseEntity<InventarioResponsePrivado> crearPrivado(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer recursoId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                inventarioService.crearInventarioPrivado(jwt, recursoId)
        );
    }
}