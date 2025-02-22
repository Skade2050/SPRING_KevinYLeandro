package com.KevinYLeandro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.SubUsoAula;
import com.KevinYLeandro.repository.SubUsoAulaRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SubUsoAulaService {

    @Autowired
    private SubUsoAulaRepository subUsoAulaRepository;

    public List<SubUsoAula> findAll() {
        return subUsoAulaRepository.findAll();
    }

    public Optional<SubUsoAula> findById(Long id) {
        return subUsoAulaRepository.findById(id);
    }

    public SubUsoAula save(SubUsoAula subUsoAula) {
        return subUsoAulaRepository.save(subUsoAula);
    }

    public void deleteById(Long id) {
        subUsoAulaRepository.deleteById(id);
    }
} 