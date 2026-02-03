package io.github.isaac.vulcano.services;


import io.github.isaac.vulcano.dtos.recurso.RecursoCreateRequest;
import io.github.isaac.vulcano.dtos.recurso.RecursoResponse;
import io.github.isaac.vulcano.dtos.recurso.RecursoUpdateRequest;
import io.github.isaac.vulcano.entities.Recurso;
import io.github.isaac.vulcano.mappers.RecursoMapper;
import io.github.isaac.vulcano.repositories.RecursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<RecursoResponse> crearRecursoBulk(List<RecursoCreateRequest> request) {
        List<Recurso> recursos = request.stream().map(recursoMapper::toEntity).toList();
        List<Recurso> recursosBd = recursoRepository.saveAll(recursos);

        return recursosBd
                .stream()
                .map(recursoMapper::toResponse)
                .toList();
    }

    // R
    public List<RecursoResponse> listarRecurso() {
        List<Recurso> recursos = recursoRepository.findAll();

        return recursos.stream()
                .map(recursoMapper::toResponse)
                .toList();
    }

    public Page<RecursoResponse> listarRecursoPaginado(Pageable pageable) {
        return recursoRepository.findAll(pageable)
                .map(recursoMapper::toResponse);
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
