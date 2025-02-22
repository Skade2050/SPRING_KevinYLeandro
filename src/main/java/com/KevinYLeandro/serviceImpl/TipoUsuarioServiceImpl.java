package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.TipoUsuario;
import com.KevinYLeandro.repository.TipoUsuarioRepository;
import com.KevinYLeandro.service.TipoUsuarioService;
import java.util.List;

@Service
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Override
    public TipoUsuario findByTipo(String tipo) {
        List<TipoUsuario> tipos = tipoUsuarioRepository.findAll();
        return tipos.stream()
                   .filter(t -> tipo.equals(t.getTipo()))
                   .findFirst()
                   .orElse(null);
    }

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        return tipoUsuarioRepository.save(tipoUsuario);
    }
} 