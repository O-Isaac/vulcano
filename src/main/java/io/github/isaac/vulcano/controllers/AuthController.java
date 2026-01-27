package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.auth.LoginRequest;
import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.RefreshToken;
import io.github.isaac.vulcano.exceptions.TokenException;
import io.github.isaac.vulcano.repositories.RefreshTokenRepository;
import io.github.isaac.vulcano.services.JugadorService;
import io.github.isaac.vulcano.services.TokenService;
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
public class AuthController {

    private final JugadorService jugadorService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Registro de nuevo jugador.
     * Si falla (usuario duplicado, etc.), el GlobalExceptionHandler responderá automáticamente.
     */
    @PostMapping("/register")
    @NullMarked
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        Jugador jugador = jugadorService.registrar(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario registrado correctamente",
                "username", jugador.getCorreo()
        ));
    }

    /**
     * Login para obtener el token JWT (validez 1 hora).
     */
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
