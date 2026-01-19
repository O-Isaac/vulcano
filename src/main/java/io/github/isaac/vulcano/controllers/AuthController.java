package io.github.isaac.vulcano.controllers;

import io.github.isaac.vulcano.dtos.auth.LoginRequest;
import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.entities.Jugador;
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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JugadorService jugadorService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

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
        String token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(Map.of(
                "access_token", token,
                "token_type", "Bearer"
        ));
    }
}