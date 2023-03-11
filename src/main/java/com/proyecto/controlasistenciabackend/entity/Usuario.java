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
}
