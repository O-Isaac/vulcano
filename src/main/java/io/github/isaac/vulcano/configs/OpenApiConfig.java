package io.github.isaac.vulcano.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Vulcano API - Sistema de Fundición y Fabricación",
        version = "0.0.1-SNAPSHOT",
        description = """
            API REST para sistema de fundición y fabricación de materiales asíncrono.
            
            ## Características principales:
            - **Autenticación JWT**: Todos los endpoints protegidos requieren un token válido
            - **Roles**: ADMIN (gestión completa) y USER (operaciones básicas)
            - **Sistema asíncrono**: Las construcciones se procesan en segundo plano
            - **Validaciones de negocio**: Verificación de recursos, créditos y duplicados
            
            ## Flujo de trabajo típico:
            1. Registrarse o iniciar sesión para obtener un token JWT
            2. Consultar recursos y planos disponibles
            3. Acumular recursos en el inventario
            4. Iniciar construcción de objetos según planos (consume recursos y créditos)
            5. El sistema procesa automáticamente las construcciones completadas
            
            ## Notas importantes:
            - Los tiempos de construcción se manejan en **milisegundos**
            - La rareza de recursos puede ser: COMUN, RARO o LEGENDARIO
            - Los planos requieren componentes (recursos) específicos para fabricarse
            """,
        contact = @Contact(
            name = "Isaac",
            url = "https://github.com/O-Isaac/vulcano"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            description = "Servidor de desarrollo local",
            url = "http://localhost:8080"
        )
    }
)
@SecurityScheme(
    name = "bearer-jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT obtenido del endpoint /api/auth/login. Incluir en el header: Authorization: Bearer {token}"
)
public class OpenApiConfig {
    // Configuración de OpenAPI mediante anotaciones
    // No requiere beans adicionales ya que SpringDoc se autoconfigura
}

