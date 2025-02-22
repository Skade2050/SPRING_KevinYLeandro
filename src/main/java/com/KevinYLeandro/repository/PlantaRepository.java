package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.Planta;
import java.util.List;

@Repository
public interface PlantaRepository extends JpaRepository<Planta, Long> {
    List<Planta> findByEdificioIdedificio(Long idedificio);
    List<Planta> findByActivoTrue();
} 