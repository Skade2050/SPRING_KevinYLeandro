package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.TipoAula;
import java.util.List;

@Repository
public interface TipoAulaRepository extends JpaRepository<TipoAula, Long> {
    List<TipoAula> findByActivoTrue();
} 