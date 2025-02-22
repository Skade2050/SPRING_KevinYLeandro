package com.KevinYLeandro.model;

import javax.persistence.*;

@Entity
@Table(name = "TIPOUSUARIO")
public class TipoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtipousuario")
    private Long idtipousuario;
    
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;
    
    @Column(name = "descripcion", length = 200)
    private String descripcion;
    
    @Column(name = "activo")
    private Boolean activo;

    // Getters y Setters
    public Long getIdtipousuario() {
        return idtipousuario;
    }

    public void setIdtipousuario(Long idtipousuario) {
        this.idtipousuario = idtipousuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
} 