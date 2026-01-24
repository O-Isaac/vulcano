package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.componente.ComponenteCreateRequest;
import io.github.isaac.vulcano.dtos.componente.ComponenteResponse;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.entities.Componente;
import io.github.isaac.vulcano.entities.Plano;
import io.github.isaac.vulcano.entities.Recurso;
import io.github.isaac.vulcano.mappers.ComponenteMapper;
import io.github.isaac.vulcano.mappers.PlanoMapper;
import io.github.isaac.vulcano.repositories.ComponenteRepository;
import io.github.isaac.vulcano.repositories.PlanoRepository;
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
    private final PlanoMapper planoMapper;
    private final PlanoRepository planoRepository;

    // Obtiene todos los componentes de la BD
    public List<ComponenteResponse> listar() {
        return componenteRepository.findAll()
                .stream()
                .map(componenteMapper::toResponse)
                .toList();
    }

    // LISTAR POR PLANO: Filtra los componentes de un plano específico
    public List<ComponenteResponse> listarPorPlano(Integer planoId) {
        return componenteRepository.findByPlanoId(planoId)
                .stream()
                .map(componenteMapper::toResponse)
                .toList();
    }

    public Optional<ComponenteResponse> obtenerPorId(Integer id) {
        return componenteRepository.findById(id)
                .map(componenteMapper::toResponse);
    }

    @Transactional
    public ComponenteResponse crearParaPlano(Integer planoId, ComponenteCreateRequest request) {
        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new EntityNotFoundException("Plano no encontrado"));

        Recurso recurso = recursoRepository.findById(request.recursoId())
                .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));

        Componente componente = componenteMapper.toEntity(request);

        componente.setPlano(plano);
        componente.setRecurso(recurso);

        return componenteMapper.toResponse(componenteRepository.save(componente));
    }

    @Transactional
    public ComponenteResponse actualizar(Integer id, ComponenteCreateRequest request) {
        Componente componente = componenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Componente no encontrado"));

        componenteMapper.partialUpdate(request, componente);

        if (request.recursoId() != null) {
            Recurso recurso = recursoRepository.findById(request.recursoId())
                    .orElseThrow(() -> new EntityNotFoundException("Recurso no encontrado"));
            componente.setRecurso(recurso);
        }

        return componenteMapper.toResponse(componenteRepository.save(componente));
    }

    public void eliminar(Integer id) {
        componenteRepository.deleteById(id);
    }
}
