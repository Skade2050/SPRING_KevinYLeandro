package com.KevinYLeandro.service;

import com.KevinYLeandro.model.Planta;
import java.util.List;
import java.util.Optional;

public interface PlantaService {
    List<Planta> findAll();
    List<Planta> findByEdificioId(Long idedificio);
    Optional<Planta> findById(Long id);
    Planta save(Planta planta);
    void deleteById(Long id);
} 