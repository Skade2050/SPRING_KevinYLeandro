package com.KevinYLeandro.service;

import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.model.Usuario;
import java.util.List;
import java.time.LocalDate;

public interface ReservaService {
    List<Reserva> findAll();
    Reserva findById(Long id);
    Reserva save(Reserva reserva);
    void deleteById(Long id);
    boolean existeReservaSuperpuesta(Reserva nuevaReserva);
    List<Reserva> findReservasByUsuario(Usuario usuario);
    List<Reserva> findReservasByAulaAndDateRange(Long aulaId, LocalDate startDate, LocalDate endDate);
    List<Reserva> findByCursoId(Long cursoId);
    List<Reserva> findByAulaId(Long aulaId);
    boolean esReservaValida(Reserva reserva);
} 