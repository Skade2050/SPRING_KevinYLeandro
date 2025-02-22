package com.KevinYLeandro.service;

import com.KevinYLeandro.model.Aula;
import java.util.List;
import java.util.Optional;

public interface AulaService {
    List<Aula> findAll();
    List<Aula> findByPlantaId(Long idplanta);
    List<Aula> findByTipoAulaId(Long idtipo);
    Optional<Aula> findById(Long id);
    Aula save(Aula aula);
    void deleteById(Long id);
    void deleteAllByPlantaId(Long plantaId);
} 