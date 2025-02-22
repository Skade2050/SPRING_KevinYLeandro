package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "RESERVA")
@Data
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idreserva;
    
    private LocalDate fechadesde;
    private LocalDate fechahasta;
    private LocalTime horadesde;
    private LocalTime horahasta;
    private Boolean validar;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "AULAidaula")
    private Aula aula;
    
    @ManyToOne
    @JoinColumn(name = "USUARIOidusuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "CURSidcurso")
    private Curso curso;
} 