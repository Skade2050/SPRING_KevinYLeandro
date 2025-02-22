package com.KevinYLeandro.service;

import com.KevinYLeandro.model.FamCurso;
import java.util.List;
import java.util.Optional;

public interface FamCursoService {
    List<FamCurso> findAll();
    Optional<FamCurso> findById(Long id);
    FamCurso save(FamCurso famCurso);
    void deleteById(Long id);
} 