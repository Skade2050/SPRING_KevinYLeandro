package com.KevinYLeandro.model;

import javax.persistence.*;

@Entity
@Table(name = "edificio")
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idedificio")
    private Long idedificio;

    @Column
    private String descripcion;

    @Column
    private Integer naulas;

    @Column
    private Integer npuertasacceso;

    @Column
    private String ubicacion;

    @Column
    private Boolean activo;

    // Getters y Setters
    public Long getIdedificio() {
        return idedificio;
    }

    public void setIdedificio(Long idedificio) {
        this.idedificio = idedificio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNaulas() {
        return naulas;
    }

    public void setNaulas(Integer naulas) {
        this.naulas = naulas;
    }

    public Integer getNpuertasacceso() {
        return npuertasacceso;
    }

    public void setNpuertasacceso(Integer npuertasacceso) {
        this.npuertasacceso = npuertasacceso;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
} 