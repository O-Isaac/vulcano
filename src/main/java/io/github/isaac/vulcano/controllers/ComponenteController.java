package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.ComponenteService;
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

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Componentes", description = "Gestión de componentes necesarios para fabricar objetos según planos")
@SecurityRequirement(name = "bearer-jwt")
public class ComponenteController {

    private final ComponenteService componenteService;

    // --- RUTAS JERÁRQUICAS (Basadas en el Plano) ---

    @Operation(
        summary = "Listar componentes de un plano",
        description = "Obtiene todos los componentes (recursos requeridos) para un plano específico. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @GetMapping("/planos/{planoId}/componentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<ComponenteResponse> listarPorPlano(
            @Parameter(description = "ID del plano", required = true)
            @PathVariable Integer planoId) {
        return ResponseListEntity.ok(componenteService.listarPorPlano(planoId));
    }

    @Operation(
        summary = "Agregar componente a un plano",
        description = "Añade un nuevo componente (recurso requerido) a un plano específico. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Componente añadido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de componente inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping("/planos/{planoId}/componentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> agregarAlPlano(
            @Parameter(description = "ID del plano", required = true)
            @PathVariable Integer planoId,
            @Valid @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteService.crearParaPlano(planoId, request));
    }

    @Operation(
        summary = "Agregar múltiples componentes a un plano",
        description = "Añade varios componentes en una sola operación a un plano. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Componentes añadidos exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de componentes inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping("/planos/{planoId}/componentes/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<ComponenteResponse> agregarAlPlanoBulk(
            @Parameter(description = "ID del plano", required = true)
            @PathVariable Integer planoId,
        @Valid @RequestBody List<ComponenteCreateRequest> request) {
        return ResponseListEntity.created(componenteService.crearParaPlanoBulk(planoId, request));
    }

    // --- RUTAS INDIVIDUALES (Basadas en el ID del Componente) ---

    @Operation(
        summary = "Listar todos los componentes",
        description = "Obtiene la lista completa de todos los componentes del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de componentes obtenida exitosamente")
    })
    @GetMapping("/componentes")
    public ResponseListEntity<ComponenteResponse> listar() {
        return ResponseListEntity.ok(
                componenteService.listar()
        );
    }

    @Operation(
        summary = "Obtener componente por ID",
        description = "Retorna la información detallada de un componente específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Componente encontrado"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado", content = @Content)
    })
    @GetMapping("/componentes/{id}")
    public ResponseEntity<ComponenteResponse> obtenerPorId(
            @Parameter(description = "ID del componente", required = true)
            @PathVariable Integer id) {
        return componenteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Actualizar componente",
        description = "Modifica los datos de un componente existente. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Componente actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PutMapping("/componentes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComponenteResponse> actualizar(
            @Parameter(description = "ID del componente a actualizar", required = true)
            @PathVariable Integer id,
            @Valid @RequestBody ComponenteCreateRequest request) {
        return ResponseEntity.ok(componenteService.actualizar(id, request));
    }

    @Operation(
        summary = "Eliminar componente",
        description = "Elimina un componente del sistema. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Componente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Componente no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @DeleteMapping("/componentes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del componente a eliminar", required = true)
            @PathVariable Integer id) {
        componenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}