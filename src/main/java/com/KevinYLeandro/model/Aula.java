package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "AULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idaula;
    
    private String nombreaula;
    private String descripcion;
    private Integer capacidad;
    private Boolean proyector;
    private Boolean smarttv;
    private Boolean hdmi;
    private Boolean appletv;
    private Boolean aireacondicionado;
    private Integer nenchufes;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "TIPOAULAidtipo")
    private TipoAula tipoAula;
    
    @ManyToOne
    @JoinColumn(name = "PLANTAidplanta")
    private Planta planta;
} 