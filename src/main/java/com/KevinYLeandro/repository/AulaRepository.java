package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.Aula;
import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByPlantaIdplanta(Long idplanta);
    List<Aula> findByActivoTrue();
    List<Aula> findByTipoAulaIdtipo(Long idtipo);
    void deleteAllByPlantaIdplanta(Long plantaId);
} 