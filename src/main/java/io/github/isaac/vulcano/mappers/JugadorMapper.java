package io.github.isaac.vulcano.mappers;

import io.github.isaac.vulcano.dtos.auth.RegisterRequest;
import io.github.isaac.vulcano.entities.Jugador;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class JugadorMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Mapeamos username -> correo
    // Ponemos valores por defecto para nivel y role
    @Mapping(target = "correo", source = "username")
    @Mapping(target = "nombre", source = "username")
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "nivel", constant = "1")
    @Mapping(target = "role", constant = "ROLE_USER")
    public abstract Jugador toEntity(RegisterRequest request);

    @Named("encodePassword")
    protected String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}