package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.recurso.RecursoCreateRequest;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoUpdateRequest;
import io.github.isaac.vulcano.dtos.response.ResponseListEntity;
import io.github.isaac.vulcano.services.RecursoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@RequiredArgsConstructor
@Tag(name = "Recursos", description = "Gestión de recursos y materiales del juego")
@SecurityRequirement(name = "bearer-jwt")
public class RecursoController {

    private final RecursoServices recursoServices;

    @Operation(
        summary = "Listar todos los recursos",
        description = "Obtiene la lista completa de recursos disponibles en el juego"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de recursos obtenida exitosamente")
    })
    @GetMapping
    public ResponseListEntity<RecursoResponse> listar() {
        return ResponseListEntity.ok(
            recursoServices.listarRecurso()
        );
    }

    @Operation(
        summary = "Listar recursos paginados",
        description = "Obtiene una página de recursos con paginación configurable"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de recursos obtenida exitosamente")
    })
    @GetMapping("/paginate")
    public ResponseEntity<Page<RecursoResponse>> listarPaginado(
            @Parameter(description = "Número de página (comienza en 0)", example = "0")
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Tamaño de la página", example = "10")
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                recursoServices.listarRecursoPaginado(pageable)
        );
    }

    @Operation(
        summary = "Obtener recurso por ID",
        description = "Retorna la información detallada de un recurso específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recurso encontrado"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecursoResponse> obtenerPorId(
            @Parameter(description = "ID del recurso", required = true)
            @PathVariable Integer id) {
        return recursoServices.listarRecursoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo recurso",
        description = "Crea un nuevo recurso en el sistema. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recurso creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de recurso inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecursoResponse> crear(@Valid @RequestBody RecursoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                recursoServices.crearRecurso(request)
        );
    }

    @Operation(
        summary = "Crear múltiples recursos",
        description = "Crea varios recursos en una sola operación. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recursos creados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de recursos inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseListEntity<RecursoResponse> crear(@Valid @RequestBody List<RecursoCreateRequest> request) {
        return ResponseListEntity.created(recursoServices.crearRecursoBulk(request));
    }

    @Operation(
        summary = "Actualizar recurso",
        description = "Modifica los datos de un recurso existente. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recurso actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecursoResponse> actualizar(
            @Valid @RequestBody RecursoUpdateRequest request,
            @Parameter(description = "ID del recurso a actualizar", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                recursoServices.editarRecurso(request, id)
        );
    }

    @Operation(
        summary = "Eliminar recurso",
        description = "Elimina un recurso del sistema. Solo accesible para ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recurso eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del recurso a eliminar", required = true)
            @PathVariable Integer id) {
        recursoServices.eliminarRecurso(id);
        return ResponseEntity.noContent().build();
    }
}

