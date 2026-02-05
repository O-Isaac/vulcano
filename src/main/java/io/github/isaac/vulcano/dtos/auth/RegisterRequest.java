package io.github.isaac.vulcano.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para registro de nuevo usuario")
public record RegisterRequest(
        @Schema(description = "Correo electrónico del nuevo usuario", example = "nuevo@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El formato del correo no es válido")
        String username,

        @Schema(description = "Contraseña (mínimo 8 caracteres)", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @Schema(description = "Confirmación de contraseña (debe coincidir)", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Debes repetir la contraseña")
        String secondPassword
) {
}