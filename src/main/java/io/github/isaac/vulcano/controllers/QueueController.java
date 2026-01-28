package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.queue.QueueCreateRequest;
import io.github.isaac.vulcano.dtos.queue.QueueResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.QueueService;
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
public class QueueController {

    private final QueueService queueService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<QueueResponse> listar(
            @RequestParam(required = false, defaultValue = "EN_CONSTRUCION") String estado
    ) {
        return ResponseListEntity.ok(
                queueService.obtenerTodasConstruciones(estado)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<QueueResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseListEntity.ok(queueService.obtenerTodasConstrucionesJugador(id));
    }

    @PostMapping
    public ResponseEntity<QueueResponse> crear(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody QueueCreateRequest request) {
       return ResponseEntity.ok(
               queueService.iniciarConstruccion(jwt, request.planoId())
       );
    }


    @GetMapping("/active")
    public ResponseEntity<List<QueueResponse>> obtenerActivos(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                queueService.obtenerActivos(jwt)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizar(@PathVariable Integer id) {
        return ResponseEntity.ok("Actualizar queue con id: " + id);
    }
}

