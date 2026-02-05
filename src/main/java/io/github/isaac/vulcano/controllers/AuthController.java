package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.auth.LoginRequest;
import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.RefreshToken;
import io.github.isaac.vulcano.exceptions.TokenException;
import io.github.isaac.vulcano.repositories.RefreshTokenRepository;
import io.github.isaac.vulcano.services.JugadorService;
import io.github.isaac.vulcano.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para el manejo de autenticación y autorización de usuarios")
public class AuthController {

    private final JugadorService jugadorService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(
        summary = "Registrar nuevo jugador",
        description = "Crea una cuenta nueva de jugador con rol USER por defecto. Requiere correo único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Jugador registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos o correo duplicado", content = @Content)
    })
    @PostMapping("/register")
    @NullMarked
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        Jugador jugador = jugadorService.registrar(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario registrado correctamente",
                "username", jugador.getCorreo()
        ));
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario y devuelve un access token JWT (válido 1 hora) y un refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa, retorna tokens"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas", content = @Content)
    })
    @PostMapping("/login")
    @NullMarked
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Autenticar credenciales (Spring Security se encarga de llamar a loadUserByUsername)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // 2. Si llegamos aquí, las credenciales son correctas. Generamos el token.
        String accessToken = tokenService.generateToken(authentication);

        // 3. Genereamos el refresh token
        Jugador jugador = (Jugador) authentication.getPrincipal();
        String refreshToken = tokenService.createRefreshToken(jugador);

        // Devolvemos
        return ResponseEntity.ok(Map.of(
                "access_token", accessToken,
                "refresh_token", refreshToken,
                "token_type", "Bearer"
        ));
    }

    @Operation(
        summary = "Refrescar token de acceso",
        description = "Genera un nuevo access token usando un refresh token válido"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Refresh token no encontrado o expirado", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestRefreshToken = request.get("refresh_token");

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(tokenService::verifyExpiration)
                .map(RefreshToken::getJugador)
                .map(jugador -> tokenService.reauthenticate(jugador, requestRefreshToken))
                .orElseThrow(() -> new TokenException("Refresh Token no encontrado en el sistema"));
    }
}
