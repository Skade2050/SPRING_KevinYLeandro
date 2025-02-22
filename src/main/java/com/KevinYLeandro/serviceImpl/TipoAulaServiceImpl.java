package com.KevinYLeandro.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.KevinYLeandro.model.TipoAula;
import com.KevinYLeandro.repository.TipoAulaRepository;
import com.KevinYLeandro.service.TipoAulaService;
import java.util.List;

@Service
public class TipoAulaServiceImpl implements TipoAulaService {
    
    @Autowired
    private TipoAulaRepository tipoAulaRepository;
    
    @Override
    public List<TipoAula> findAll() {
        return tipoAulaRepository.findAll();
    }
    
    @Override
    public TipoAula findById(Long id) {
        return tipoAulaRepository.findById(id).orElse(null);
    }
    
    @Override
    public TipoAula save(TipoAula tipoAula) {
        return tipoAulaRepository.save(tipoAula);
    }
    
    @Override
    public void deleteById(Long id) {
        tipoAulaRepository.deleteById(id);
    }
} 