package com.KevinYLeandro.service;

import com.KevinYLeandro.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario findByEmail(String email);
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
    void toggleActivo(Long id);
    void delete(Long id);
    Optional<Usuario> findById(Long id);
} 