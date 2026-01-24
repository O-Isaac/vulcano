package io.github.isaac.vulcano.dtos.recurso;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Recurso}
 */
public record RecursoCreateRequest(
        @NotNull(message = "El nombre es obligatorio")
        @NotEmpty(message = "El nombre no puede estar vacio") String nombre,
        @NotNull(message = "La descripción es obligatoria")
        @Size(message = "La descripción no cumple con un minimo de 10 a 50 caracteres", min = 10, max = 50)
        @NotEmpty(message = "La descripción no puede estar vacia") String desc,
        @NotNull(message = "La rareza es obligatoria")
        @Pattern(message = "No cumple con el patron requerido", regexp = "(COMUN|RARO|LEGENDARIO)")
        @NotEmpty String rareza) implements Serializable {
}