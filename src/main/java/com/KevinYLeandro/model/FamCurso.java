package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "FAMCURSO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idfamcurso;
    
    private String descripcion;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "SUBUSOAULAidsubuso", nullable = false)
    private SubUsoAula subUsoAula;
} 