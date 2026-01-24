package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.entities.Componente;
import io.github.isaac.vulcano.entities.Recurso;
import io.github.isaac.vulcano.mappers.ComponenteMapper;
import io.github.isaac.vulcano.repositories.ComponenteRepository;
import io.github.isaac.vulcano.repositories.RecursoRepository;
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
    private final RecursoRepository recursoRepository;

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

    @Transactional
    public ComponenteResponse crear(ComponenteCreateRequest request) {
        // 1. Convertimos el DTO a entidad (solo tendrá la 'cantidad')
        Componente componente = componenteMapper.toEntity(request);

        // 2. Buscamos el recurso por el ID que viene en el request
        if (request.recursoId() != null) {
            Recurso recurso = recursoRepository.findById(request.recursoId())
                    .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));

            // 3. Usamos metodo auxiliar para mantener la bidirecionalidad
            componente.setRecurso(recurso);
        }

        // 4. Guardamos y devolvemos
        return componenteMapper.toResponse(componenteRepository.save(componente));
    }

    @Transactional
    public ComponenteResponse actualizar(Integer id, ComponenteCreateRequest request) {
        // 1. Buscamos la entidad existente
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Componente no encontrado"));

        // 2. Mapeamos datos básicos (el mapper debe ignorar 'recurso' y 'plano')
        componenteMapper.partialUpdate(request, componente);

        // 3. Establecemos la relación de forma fluida
        if (request.recursoId() != null) {
            Recurso recurso = recursoRepository.getReferenceById(request.recursoId());
            componente.setRecurso(recurso);
        }

        // 4. Guardamos. Si el recursoId no existe en BD, saltará EntityNotFoundException aquí.
        return componenteMapper.toResponse(
                componenteRepository.save(componente)
        );
    }

    public void eliminar(Integer id) {
        componenteRepository.deleteById(id);
    }
}
