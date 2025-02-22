package com.KevinYLeandro.repository;

import com.KevinYLeandro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Usuario findByEmail(String email);
    
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.activo = CASE WHEN u.activo = true THEN false ELSE true END WHERE u.idusuario = ?1")
    void toggleActivo(Long id);
} 