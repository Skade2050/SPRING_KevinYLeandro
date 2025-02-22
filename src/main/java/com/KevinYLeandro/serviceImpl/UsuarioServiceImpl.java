package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.Usuario;
import com.KevinYLeandro.repository.UsuarioRepository;
import com.KevinYLeandro.service.UsuarioService;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario findByEmail(String email) {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return usuarios.stream()
                         .filter(u -> email.equals(u.getEmail()))
                         .filter(u -> u.getTipoUsuario() != null) // Filtra solo usuarios con tipo
                         .findFirst()
                         .orElse(
                             usuarios.stream()
                                    .filter(u -> email.equals(u.getEmail()))
                                    .findFirst()
                                    .orElse(null)
                         );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Usuario save(Usuario usuario) {
        // Si es un usuario nuevo (sin ID), establecer valores por defecto
        if (usuario.getIdusuario() == null) {
            usuario.setActivo(true);
            usuario.setFechaactivacion(new Date());
            usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        }
        // Si es una actualización, la contraseña ya viene encriptada del controlador si fue modificada
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public void toggleActivo(Long id) {
        usuarioRepository.toggleActivo(id);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
} 