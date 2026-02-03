package io.github.isaac.vulcano.repositories;

import io.github.isaac.vulcano.entities.Recurso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecursoRepository extends JpaRepository<Recurso, Integer> {
    List<Recurso> findAll();
    Page<Recurso> findAll(Pageable pageable);
}