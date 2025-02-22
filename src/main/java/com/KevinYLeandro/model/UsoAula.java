package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "USOAULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoAula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iduso;
    
    private String descripcion;
    private Boolean activo;
} 