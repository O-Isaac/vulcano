package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.plano.PlanoCreateRequest;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.PlanoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planos")
@RequiredArgsConstructor
@Tag(name = "Planos", description = "Gestión de planos de construcción y fabricación")
@SecurityRequirement(name = "bearer-jwt")
public class PlanoController {

    private final PlanoService planoService;

    @Operation(
        summary = "Listar todos los planos",
        description = "Obtiene la lista completa de planos de fabricación disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de planos obtenida exitosamente")
    })
    @GetMapping
    public ResponseListEntity<PlanoResponse> listar() {
        return ResponseListEntity.ok(planoService.listar());
    }

    @Operation(
        summary = "Obtener plano por ID",
        description = "Retorna la información detallada de un plano específico, incluyendo componentes requeridos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plano encontrado"),
        @ApiResponse(responseCode = "404", description = "Plano no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponse> obtenerPorId(
            @Parameter(description = "ID del plano", required = true)
            @PathVariable Integer id) {
        return planoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo plano",
        description = "Crea un nuevo plano de fabricación en el sistema. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plano creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de plano inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponse> crear(@RequestBody @Valid PlanoCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(planoService.crear(request));
    }

    @Operation(
        summary = "Actualizar plano",
        description = "Modifica los datos de un plano existente. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plano actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plano no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoResponse> actualizar(
            @Parameter(description = "ID del plano a actualizar", required = true)
            @PathVariable Integer id,
            @RequestBody @Valid PlanoCreateRequest request) {
        return ResponseEntity.ok(planoService.actualizar(id, request));
    }

    @Operation(
        summary = "Eliminar plano",
        description = "Elimina un plano del sistema. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Plano eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plano no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(
            @Parameter(description = "ID del plano a eliminar", required = true)
            @PathVariable Integer id) {
        planoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

