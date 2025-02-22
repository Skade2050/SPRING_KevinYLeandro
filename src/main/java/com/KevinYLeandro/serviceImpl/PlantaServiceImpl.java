package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.repository.PlantaRepository;
import com.KevinYLeandro.service.PlantaService;

@Service
public class PlantaServiceImpl implements PlantaService {
    
    @Autowired
    private PlantaRepository plantaRepository;
    
    @Override
    public List<Planta> findAll() {
        return plantaRepository.findAll();
    }
    
    @Override
    public List<Planta> findByEdificioId(Long idedificio) {
        return plantaRepository.findByEdificioIdedificio(idedificio);
    }
    
    @Override
    public Optional<Planta> findById(Long id) {
        return plantaRepository.findById(id);
    }
    
    @Override
    public Planta save(Planta planta) {
        return plantaRepository.save(planta);
    }
    
    @Override
    public void deleteById(Long id) {
        plantaRepository.deleteById(id);
    }
} 