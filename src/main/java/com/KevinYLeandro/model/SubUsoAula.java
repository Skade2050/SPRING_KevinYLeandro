package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "SUBUSOAULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubUsoAula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idsubuso;
    
    private String descripcion;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "USOAULAiduso", nullable = false)
    private UsoAula usoAula;
} 