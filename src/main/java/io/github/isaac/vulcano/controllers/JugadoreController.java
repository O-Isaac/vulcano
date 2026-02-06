package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.jugador.JugadorResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.JugadorService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
@Tag(name = "Jugadores", description = "Gestión de jugadores y perfiles de usuario")
@SecurityRequirement(name = "bearer-jwt")
public class JugadoreController {

    private final JugadorService jugadorService;

    @Operation(
        summary = "Listar todos los jugadores",
        description = "Obtiene la lista completa de jugadores registrados. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<JugadorResponse> getJugadores() {
        return ResponseListEntity.ok(jugadorService.getJugadores());
    }

    @Operation(
        summary = "Añadir créditos a un jugador",
        description = "Incrementa el saldo de créditos de un jugador específico. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Créditos añadidos exitosamente"),
        @ApiResponse(responseCode = "404", description = "Jugador no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PatchMapping("/creditos/{jugadorId}/{cantidad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JugadorResponse> addCreditos(
            @Parameter(description = "Cantidad de créditos a añadir", required = true)
            @PathVariable Long cantidad,
            @Parameter(description = "ID del jugador", required = true)
            @PathVariable Integer jugadorId
    ) {
        return ResponseEntity.ok(
                jugadorService.addCreditos(jugadorId, cantidad)
        );
    }

    @Operation(
        summary = "Obtener información del usuario autenticado",
        description = "Retorna la información del perfil del usuario que está autenticado actualmente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información del usuario obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<JugadorResponse> getMe(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                jugadorService.getInfoMe(jwt)
        );
    }
}

