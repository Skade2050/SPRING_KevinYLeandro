package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.Aula;
import com.KevinYLeandro.repository.AulaRepository;
import com.KevinYLeandro.service.AulaService;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AulaServiceImpl implements AulaService {
    
    @Autowired
    private AulaRepository aulaRepository;
    
    @Override
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }
    
    @Override
    public List<Aula> findByPlantaId(Long idplanta) {
        return aulaRepository.findByPlantaIdplanta(idplanta);
    }
    
    @Override
    public List<Aula> findByTipoAulaId(Long idtipo) {
        return aulaRepository.findByTipoAulaIdtipo(idtipo);
    }
    
    @Override
    public Optional<Aula> findById(Long id) {
        return aulaRepository.findById(id);
    }
    
    @Override
    public Aula save(Aula aula) {
        return aulaRepository.save(aula);
    }
    
    @Override
    public void deleteById(Long id) {
        aulaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByPlantaId(Long plantaId) {
        aulaRepository.deleteAllByPlantaIdplanta(plantaId);
    }
} 