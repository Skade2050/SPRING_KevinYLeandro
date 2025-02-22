package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "CURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcurso;
    
    private String descripcion;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "FAMCURSOidfamcurso", nullable = false)
    private FamCurso famCurso;
    
    @ManyToOne
    @JoinColumn(name = "AULAidaula")
    private Aula aula;
} 