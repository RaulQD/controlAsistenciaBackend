package com.proyecto.controlasistenciabackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tb_empleado")
@Getter
@Setter
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEmpleado;
    private String nombre;
    private String apellido;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false, unique = true)
    private String correo;
    private String contacto;
    private String direccion;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
    private Date fechaNacimiento;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Lima")
    private Date fechaRegistro;

    private double tarifa_hora;
    private String foto;
    @ManyToOne
    @JoinColumn(name = "idArea")
    private Area area;
    @ManyToOne
    @JoinColumn(name = "idCargo")
    private Cargo cargo;
    private int estado;
}
