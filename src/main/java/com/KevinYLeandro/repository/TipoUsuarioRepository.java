package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.KevinYLeandro.model.TipoUsuario;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    TipoUsuario findByTipo(String tipo);
} 