package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.Curso;
import com.KevinYLeandro.repository.CursoRepository;
import com.KevinYLeandro.service.CursoService;
import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Override
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }
    
    @Override
    public Optional<Curso> findById(Long id) {
        return cursoRepository.findById(id);
    }
    
    @Override
    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }
    
    @Override
    public void deleteById(Long id) {
        cursoRepository.deleteById(id);
    }
    
    @Override
    public List<Curso> findByFamCursoId(Long idfamcurso) {
        return cursoRepository.findByFamCursoIdfamcurso(idfamcurso);
    }
} 