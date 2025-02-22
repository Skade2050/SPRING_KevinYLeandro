package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.FamCurso;
import java.util.List;

@Repository
public interface FamCursoRepository extends JpaRepository<FamCurso, Long> {
    List<FamCurso> findByActivoTrue();
} 