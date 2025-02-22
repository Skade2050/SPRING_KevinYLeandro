package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.FamCurso;
import com.KevinYLeandro.repository.FamCursoRepository;
import com.KevinYLeandro.service.FamCursoService;
import java.util.List;
import java.util.Optional;

@Service
public class FamCursoServiceImpl implements FamCursoService {
    
    @Autowired
    private FamCursoRepository famCursoRepository;
    
    @Override
    public List<FamCurso> findAll() {
        return famCursoRepository.findAll();
    }
    
    @Override
    public Optional<FamCurso> findById(Long id) {
        return famCursoRepository.findById(id);
    }
    
    @Override
    public FamCurso save(FamCurso famCurso) {
        return famCursoRepository.save(famCurso);
    }
    
    @Override
    public void deleteById(Long id) {
        famCursoRepository.deleteById(id);
    }
} 