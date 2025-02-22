package com.KevinYLeandro.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PLANTA")
@Data
public class Planta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idplanta;
    
    private String descripcion;
    private Integer naulasdocentes;
    private Integer naulaauxiliares;
    private Boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "EDIFICIOidedificio", nullable = false)
    private Edificio edificio;

    // Getters y Setters
    public Long getIdplanta() {
        return idplanta;
    }

    public void setIdplanta(Long idplanta) {
        this.idplanta = idplanta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNaulasdocentes() {
        return naulasdocentes;
    }

    public void setNaulasdocentes(Integer naulasdocentes) {
        this.naulasdocentes = naulasdocentes;
    }

    public Integer getNaulaauxiliares() {
        return naulaauxiliares;
    }

    public void setNaulaauxiliares(Integer naulaauxiliares) {
        this.naulaauxiliares = naulaauxiliares;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }
} 