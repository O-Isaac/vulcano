package io.github.isaac.vulcano.services;


import io.github.isaac.vulcano.dtos.recursos.RecursoCreateRequest;
import io.github.isaac.vulcano.dtos.recursos.RecursoResponse;
import io.github.isaac.vulcano.dtos.recursos.RecursoUpdateRequest;
import io.github.isaac.vulcano.entities.Recurso;
import io.github.isaac.vulcano.mappers.RecursoMapper;
import io.github.isaac.vulcano.repositories.RecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecursoServices {
    private final RecursoRepository recursoRepository;
    private final RecursoMapper recursoMapper;

    // C
    public RecursoResponse crearRecurso(RecursoCreateRequest request) {
        Recurso recurso = recursoMapper.toEntity(request);
        Recurso recursoDatabase = recursoRepository.save(recurso);

        return recursoMapper.toResponse(recursoDatabase);
    }

    // R
    public List<RecursoResponse> listarRecurso() {
        List<Recurso> recursos = recursoRepository.findAll();

        return recursos.stream()
                .map(recursoMapper::toResponse)
                .toList();
    }

    public Optional<RecursoResponse> listarRecursoById(Integer id) {
        return recursoRepository.findById(id)
                .map(recursoMapper::toResponse);
    }

    // U
    @Transactional
    public RecursoResponse editarRecurso(RecursoUpdateRequest request, Integer id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        recursoMapper.partialUpdate(request, recurso);

        return  recursoMapper.toResponse(
            recursoRepository.save(recurso)
        );
    }

    // D
    public void eliminarRecurso(Integer recursoId) {
        recursoRepository.deleteById(recursoId);
    }
}
