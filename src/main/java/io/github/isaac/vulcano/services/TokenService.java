package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.entities.Jugador;
import io.github.isaac.vulcano.entities.RefreshToken;
import io.github.isaac.vulcano.exceptions.BadRequestException;
import io.github.isaac.vulcano.exceptions.TokenException;
import io.github.isaac.vulcano.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final RefreshTokenRepository refreshTokenRepository; // Debes crear este Repo

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        // Configurar 1 hora de duración (3600 segundos)
        Instant expiry = now.plus(1, ChronoUnit.HOURS);

        // Extraer roles para meterlos en el token
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Obtenemos el jugador
        Jugador jugador = (Jugador) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiry) // <--- CADUCIDAD
                .subject(authentication.getName())
                .claim("roles", scope)
                .claim("nivel", jugador != null ? jugador.getNivel() : 0)
                .build();

        // Especificar explícitamente el algoritmo HS256
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Transactional // Sigue siendo vital para la persistencia
    public String createRefreshToken(Jugador jugador) {
        // 1. Buscamos si el jugador ya tiene un token
        RefreshToken refreshToken = refreshTokenRepository.findByJugador(jugador)
                .orElse(new RefreshToken()); // Si no tiene, creamos la instancia

        // 2. Seteamos/Actualizamos los valores
        refreshToken.setJugador(jugador);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenException("El Refresh Token ha expirado. Por favor, inicie sesión de nuevo.");
        }
        return token;
    }

    public ResponseEntity<?> reauthenticate(Jugador jugador, String refreshToken) {
        Authentication auth = new UsernamePasswordAuthenticationToken(jugador, null, jugador.getAuthorities());
        String token = this.generateToken(auth);

        return ResponseEntity.ok(Map.of(
                "access_token", token,
                "refresh_token", refreshToken // Opcional: rotar el refresh token aquí también
        ));
    }
}
