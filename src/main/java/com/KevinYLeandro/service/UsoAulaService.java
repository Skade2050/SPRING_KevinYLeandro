package com.KevinYLeandro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.UsoAula;
import com.KevinYLeandro.repository.UsoAulaRepository;
import java.util.List;

@Service
public class UsoAulaService {

    @Autowired
    private UsoAulaRepository usoAulaRepository;

    public List<UsoAula> findAll() {
        return usoAulaRepository.findAll();
    }

    public UsoAula save(UsoAula usoAula) {
        return usoAulaRepository.save(usoAula);
    }
} 