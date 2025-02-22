package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.UsoAula;
import java.util.List;

@Repository
public interface UsoAulaRepository extends JpaRepository<UsoAula, Long> {
    List<UsoAula> findByActivoTrue();
} 