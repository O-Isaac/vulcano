package io.github.isaac.vulcano.dtos.plano;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link io.github.isaac.vulcano.entities.Plano}
 */
@Schema(description = "Datos para crear o actualizar un plano de fabricación")
public record PlanoCreateRequest(
        @Schema(description = "Coste en créditos para fabricar el objeto", example = "250000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El coste no puede ser nulo")
        @PositiveOrZero(message = "El coste debe ser igual o mayor a cero")
        Long coste,

        @Schema(description = "Descripción del plano", example = "El plano maestro para ensamblar Excalibur Prime", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La descripción es obligatoria")
        @NotEmpty(message = "La descripción no puede estar vacía")
        String desc,

        @Schema(description = "Nombre del plano", example = "Plano de Excalibur Prime", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El nombre es obligatorio")
        @NotEmpty(message = "El nombre no puede estar vacío")
        String nombre,

        @Schema(description = "Tiempo de construcción en milisegundos", example = "30000", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El tiempo de construcción es obligatorio")
        @PositiveOrZero(message = "El tiempo de construcción no puede ser negativo")
        Long tiempoConstrucion,

        @Schema(description = "ID del recurso que se fabrica con este plano", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El recursoFabricadoId es obligatorio")
        @Positive(message = "El recursoFabricadoId debe ser mayor a cero")
        Integer recursoFabricadoId
) implements Serializable {
}