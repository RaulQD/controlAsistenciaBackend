package com.proyecto.controlasistenciabackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String dni;
    @Column(unique = true)
    private String correo;
    private String contacto;
    private String direccion;

    @Temporal(TemporalType.DATE)    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
    private Date fechaNacimiento;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
    private Date fechaRegistro;
    private double tarifa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Area area;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo" )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cargo cargo;
    private String estado;
    private String password;

    @ManyToMany
    @JoinTable(name = "usuario_has_rol", joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();

    public Usuario(){

    }

    public Usuario(String nombre, String apellido, String dni, String correo, String contacto, String direccion, Date fechaNacimiento, Date fechaRegistro, double tarifa, Area area, Cargo cargo, String estado, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
        this.contacto = contacto;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.tarifa = tarifa;
        this.area = area;
        this.cargo = cargo;
        this.estado = estado;
        this.password = password;
    }
}
