package com.KevinYLeandro.service;

import com.KevinYLeandro.model.TipoAula;
import java.util.List;

public interface TipoAulaService {
    List<TipoAula> findAll();
    TipoAula findById(Long id);
    TipoAula save(TipoAula tipoAula);
    void deleteById(Long id);
} 