package com.KevinYLeandro.service;

import com.KevinYLeandro.model.TipoUsuario;

public interface TipoUsuarioService {
    TipoUsuario findByTipo(String tipo);
    TipoUsuario save(TipoUsuario tipoUsuario);
} 