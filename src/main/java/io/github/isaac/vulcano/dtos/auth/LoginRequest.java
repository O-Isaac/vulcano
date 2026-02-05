package io.github.isaac.vulcano.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos de inicio de sesión")
public record LoginRequest(
        @Schema(description = "Contraseña del usuario", example = "miPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @Schema(description = "Correo electrónico del usuario", example = "usuario@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es válido")
        String username
) {
}
