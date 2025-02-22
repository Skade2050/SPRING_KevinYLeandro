package com.KevinYLeandro.service;

import java.util.List;
import java.util.Optional;

import com.KevinYLeandro.model.Edificio;

public interface EdificioService {
    
    List<Edificio> findAll();
    
    Optional<Edificio> findById(Long id);
    
    Edificio save(Edificio edificio);
    
    void deleteById(Long id);
} 