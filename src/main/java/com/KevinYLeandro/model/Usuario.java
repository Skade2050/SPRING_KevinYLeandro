package com.KevinYLeandro.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USUARIO")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Long idusuario;
    
    @Column(name = "nombre", length = 30)
    private String nombre;
    
    @Column(name = "apellido", length = 150)
    private String apellido;
    
    @Column(name = "fechaactivacion")
    @Temporal(TemporalType.DATE)
    private Date fechaactivacion;
    
    @Column(name = "fechadesactivacion")
    @Temporal(TemporalType.DATE)
    private Date fechadesactivacion;
    
    @Column(name = "dni", length = 9)
    private String dni;
    
    @Column(name = "email", length = 200)
    private String email;
    
    @Column(name = "contrasenya", length = 60)
    private String contrasenya;
    
    @Column(name = "activo")
    private Boolean activo;
    
    @Column(name = "telefono", length = 9)
    private String telefono;
    
    @ManyToOne
    @JoinColumn(name = "TIPOUSUARIOidtipousuario")
    private TipoUsuario tipoUsuario;

    // Getters y Setters
    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaactivacion() {
        return fechaactivacion;
    }

    public void setFechaactivacion(Date fechaactivacion) {
        this.fechaactivacion = fechaactivacion;
    }

    public Date getFechadesactivacion() {
        return fechadesactivacion;
    }

    public void setFechadesactivacion(Date fechadesactivacion) {
        this.fechadesactivacion = fechadesactivacion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
} 