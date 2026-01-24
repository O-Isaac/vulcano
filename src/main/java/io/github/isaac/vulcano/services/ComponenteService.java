package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.entities.Componente;
import io.github.isaac.vulcano.mappers.ComponenteMapper;
import io.github.isaac.vulcano.repositories.ComponenteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComponenteService {

    private final ComponenteRepository componenteRepository;
    private final ComponenteMapper componenteMapper;

    public List<ComponenteResponse> listar() {
        return componenteRepository.findAll()
                .stream()
                .map(componenteMapper::toResponse)
                .toList();
    }

    public Optional<ComponenteResponse> obtenerPorId(Integer id) {
        return componenteRepository.findById(id)
                .map(componenteMapper::toResponse);
    }

    public ComponenteResponse crear(ComponenteCreateRequest request) {
        Componente componente = componenteMapper.toEntity(request);

        return componenteMapper.toResponse(
                componenteRepository.save(componente)
        );
    }

    @Transactional
    public ComponenteResponse actualizar(Integer id, ComponenteCreateRequest request) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        componenteMapper.partialUpdate(request, componente);

        return componenteMapper.toResponse(
                componenteRepository.save(componente)
        );
    }

    public void eliminar(Integer id) {
        componenteRepository.deleteById(id);
    }
}
