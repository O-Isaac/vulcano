package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.queue.QueueCreateRequest;
import io.github.isaac.vulcano.dtos.queue.QueueResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@AllArgsConstructor
@Tag(name = "Cola de Construcción", description = "Gestión de la cola de fabricación y construcción de objetos")
@SecurityRequirement(name = "bearer-jwt")
public class QueueController {

    private final QueueService queueService;

    @Operation(
        summary = "Listar todas las construcciones",
        description = "Obtiene la lista de todas las construcciones en cola, filtrable por estado. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de construcciones obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<QueueResponse> listar(
            @Parameter(description = "Estado de la construcción", example = "EN_CONSTRUCCION")
            @RequestParam(required = false, defaultValue = "EN_CONSTRUCION") String estado
    ) {
        return ResponseListEntity.ok(
                queueService.obtenerTodasConstruciones(estado)
        );
    }

    @Operation(
        summary = "Obtener construcciones de un jugador",
        description = "Retorna todas las construcciones de un jugador específico. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construcciones del jugador obtenidas exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<QueueResponse> obtenerPorId(
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer id) {
        return ResponseListEntity.ok(queueService.obtenerTodasConstrucionesJugador(id));
    }

    @Operation(
        summary = "Iniciar construcción de un plano",
        description = "Inicia la fabricación de un objeto según un plano. Requiere créditos y componentes suficientes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construcción iniciada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Créditos o recursos insuficientes, o plano ya en construcción", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Plano no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<QueueResponse> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody QueueCreateRequest request) {
       return ResponseEntity.ok(
               queueService.iniciarConstruccion(jwt, request.planoId())
       );
    }

    @Operation(
        summary = "Obtener construcciones activas del usuario",
        description = "Retorna todas las construcciones activas (en progreso) del jugador autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Construcciones activas obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/active")
    public ResponseEntity<List<QueueResponse>> obtenerActivos(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                queueService.obtenerActivos(jwt)
        );
    }

    @Operation(
        summary = "Actualizar construcción (placeholder)",
        description = "Endpoint placeholder para futuras actualizaciones de construcción. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Placeholder - No implementado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizar(
            @Parameter(description = "ID de la construcción", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar queue con id: " + id);
    }
}

