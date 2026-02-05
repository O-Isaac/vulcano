package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.jugador.JugadorResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.JugadorService;
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
public class JugadoreController {

    private final JugadorService jugadorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<JugadorResponse> getJugadores() {
        return ResponseListEntity.ok(jugadorService.getJugadores());
    }

    @PatchMapping("/creditos/{jugadorId}/{cantidad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JugadorResponse> addCreditos(
            @PathVariable long cantidad,
            @PathVariable int jugadorId
    ) {
        return ResponseEntity.ok(
                jugadorService.addCreditos(jugadorId, cantidad)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<JugadorResponse> getMe(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                jugadorService.getInfoMe(jwt)
        );
    }
}

