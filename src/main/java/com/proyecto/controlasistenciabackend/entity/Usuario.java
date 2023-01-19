package com.proyecto.controlasistenciabackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

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

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;
    @ManyToOne
    @JoinColumn(name = "id_cargo" )
    private Cargo cargo;
    private String estado;
    private String usuario;
    private String contrasena;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_has_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();

    @Transient
    private String nombreCompleto;
    public Usuario() {

    }

    public Usuario(int idUsuario, String nombre, String apellido, String dni, String correo, String contacto, String direccion, Date fechaNacimiento, Date fechaRegistro, double tarifa, Area area, Cargo cargo, String estado, String usuario, String contrasena) {
        this.idUsuario = idUsuario;
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
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
