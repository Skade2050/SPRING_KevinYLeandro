package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TIPOAULA")
@Data
public class TipoAula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtipo;
    
    private String nombretipo;
    private String descripcion;
    private Boolean activo;
} 