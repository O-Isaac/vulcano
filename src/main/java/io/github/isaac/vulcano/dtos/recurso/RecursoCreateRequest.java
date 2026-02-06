package io.github.isaac.vulcano.dtos.recurso;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Recurso}
 */
@Schema(description = "Datos para crear un nuevo recurso")
public record RecursoCreateRequest(
        @Schema(description = "Nombre del recurso", example = "Ferrita", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El nombre es obligatorio")
        @NotEmpty(message = "El nombre no puede estar vacio")
        String nombre,

        @Schema(description = "Descripción del recurso (10-50 caracteres)", example = "Material básico para la fabricación", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La descripción es obligatoria")
        @Size(message = "La descripción no cumple con un minimo de 10 a 50 caracteres", min = 10, max = 50)
        @NotEmpty(message = "La descripción no puede estar vacia")
        String desc,

        @Schema(description = "Rareza del recurso", example = "COMUN", allowableValues = {"COMUN", "RARO", "EPICO", "LEGENDARIO"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La rareza es obligatoria")
        @Pattern(message = "No cumple con el patron requerido", regexp = "(COMUN|RARO|EPICO|LEGENDARIO)")
        @NotEmpty
        String rareza
) implements Serializable {
}