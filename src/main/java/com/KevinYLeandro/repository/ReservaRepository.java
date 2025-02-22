package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.KevinYLeandro.model.Reserva;
import com.KevinYLeandro.model.Aula;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.KevinYLeandro.model.Usuario;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByAulaAndActivoTrueAndValidarFalseAndFechadesdeAndHoradesdeAndHorahasta(
        Aula aula,
        LocalDate fechadesde,
        LocalTime horadesde,
        LocalTime horahasta
    );

    List<Reserva> findByUsuarioAndActivoTrueOrderByFechadesdeDesc(Usuario usuario);

    @Query("SELECT r FROM Reserva r WHERE r.aula.idaula = :aulaId " +
           "AND r.fechadesde >= :startDate AND r.fechahasta <= :endDate " +
           "AND r.activo = true")
    List<Reserva> findReservasByAulaAndDateRange(
        @Param("aulaId") Long aulaId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r " +
           "WHERE r.aula = :aula AND r.activo = true AND r.idreserva != :idReserva " +
           "AND r.fechadesde <= :fechahasta AND r.fechahasta >= :fechadesde " +
           "AND ((r.horadesde < :horahasta AND r.horahasta > :horadesde) " +
           "OR (r.horadesde = :horadesde OR r.horahasta = :horahasta))")
    boolean existeReservaSuperpuesta(
        @Param("aula") Aula aula,
        @Param("fechadesde") LocalDate fechadesde,
        @Param("fechahasta") LocalDate fechahasta,
        @Param("horadesde") LocalTime horadesde,
        @Param("horahasta") LocalTime horahasta,
        @Param("idReserva") Long idReserva
    );

    List<Reserva> findByCursoIdcurso(Long idcurso);

    List<Reserva> findByAulaIdaula(Long idaula);
} 