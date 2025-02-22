package com.KevinYLeandro.service;

import com.KevinYLeandro.model.Curso;
import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> findAll();
    Optional<Curso> findById(Long id);
    Curso save(Curso curso);
    void deleteById(Long id);
    List<Curso> findByFamCursoId(Long famCursoId);
} 