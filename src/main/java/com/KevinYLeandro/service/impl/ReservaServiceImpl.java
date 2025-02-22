package com.KevinYLeandro.service.impl;

import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.model.Usuario;
import com.KevinYLeandro.repository.ReservaRepository;
import com.KevinYLeandro.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

@Service
public class ReservaServiceImpl implements ReservaService {
    
    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @Override
    public Reserva findById(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Override
    public void deleteById(Long id) {
        reservaRepository.deleteById(id);
    }

    @Override
    public boolean existeReservaSuperpuesta(Reserva nuevaReserva) {
        Long idReserva = nuevaReserva.getIdreserva() != null ? nuevaReserva.getIdreserva() : -1L;
        
        return reservaRepository.existeReservaSuperpuesta(
            nuevaReserva.getAula(),
            nuevaReserva.getFechadesde(),
            nuevaReserva.getFechahasta(),
            nuevaReserva.getHoradesde(),
            nuevaReserva.getHorahasta(),
            idReserva
        );
    }

    @Override
    public boolean esReservaValida(Reserva reserva) {
        if (reserva.getFechahasta().isBefore(reserva.getFechadesde())) {
            return false;
        }
        
        if (reserva.getFechadesde().equals(reserva.getFechahasta()) && 
            !reserva.getHorahasta().isAfter(reserva.getHoradesde())) {
            return false;
        }
        
        return !existeReservaSuperpuesta(reserva);
    }

    @Override
    public List<Reserva> findReservasByUsuario(Usuario usuario) {
        return reservaRepository.findByUsuarioAndActivoTrueOrderByFechadesdeDesc(usuario);
    }

    @Override
    public List<Reserva> findReservasByAulaAndDateRange(Long aulaId, LocalDate startDate, LocalDate endDate) {
        return reservaRepository.findReservasByAulaAndDateRange(aulaId, startDate, endDate);
    }

    @Override
    public List<Reserva> findByCursoId(Long cursoId) {
        return reservaRepository.findByCursoIdcurso(cursoId);
    }

    @Override
    public List<Reserva> findByAulaId(Long aulaId) {
        return reservaRepository.findByAulaIdaula(aulaId);
    }
} 