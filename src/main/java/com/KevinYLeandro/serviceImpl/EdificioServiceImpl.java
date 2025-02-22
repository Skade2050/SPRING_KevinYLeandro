package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.KevinYLeandro.model.Edificio;
import com.KevinYLeandro.model.Planta;
import com.KevinYLeandro.repository.EdificioRepository;
import com.KevinYLeandro.repository.PlantaRepository;
import com.KevinYLeandro.service.EdificioService;

import java.util.List;
import java.util.Optional;

@Service
public class EdificioServiceImpl implements EdificioService {

    @Autowired
    private EdificioRepository edificioRepository;

    @Autowired
    private PlantaRepository plantaRepository;

    @Override
    public List<Edificio> findAll() {
        return edificioRepository.findAll();
    }

    @Override
    public Optional<Edificio> findById(Long id) {
        return edificioRepository.findById(id);
    }

    @Override
    public Edificio save(Edificio edificio) {
        return edificioRepository.save(edificio);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Primero eliminamos todas las plantas asociadas
        List<Planta> plantas = plantaRepository.findByEdificioIdedificio(id);
        for(Planta planta : plantas) {
            plantaRepository.deleteById(planta.getIdplanta());
        }
        // Luego eliminamos el edificio
        edificioRepository.deleteById(id);
    }
} 