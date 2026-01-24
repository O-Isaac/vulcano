package io.github.isaac.vulcano.services;

import io.github.isaac.vulcano.dtos.plano.PlanoCreateRequest;
import io.github.isaac.vulcano.dtos.plano.PlanoResponse;
import io.github.isaac.vulcano.entities.Plano;
import io.github.isaac.vulcano.mappers.PlanoMapper;
import io.github.isaac.vulcano.repositories.PlanoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;
    private final PlanoMapper planoMapper;

    public List<PlanoResponse> listar() {
        return planoRepository.findAll()
                .stream()
                .map(planoMapper::toResponse)
                .toList();
    }

    public Optional<PlanoResponse> obtenerPorId(Integer id) {
        return planoRepository.findById(id)
                .map(planoMapper::toResponse);
    }

    public PlanoResponse crear(PlanoCreateRequest request) {
        Plano plano = planoMapper.toEntity(request);

        return planoMapper.toResponse(
                planoRepository.save(plano)
        );
    }

    @Transactional
    public PlanoResponse actualizar(Integer id, PlanoCreateRequest request) {
        Plano plano = planoRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        planoMapper.partialUpdate(request, plano);

        return planoMapper.toResponse(
                planoRepository.save(plano)
        );
    }

    public void eliminar(Integer id) {
        planoRepository.deleteById(id);
    }

}
