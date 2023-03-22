package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;
    @Enumerated(EnumType.STRING)
    private ERol nombre_rol;

    public Rol(){

    }

    public Rol(int idRol, ERol nombre_rol) {
        this.idRol = idRol;
        this.nombre_rol = nombre_rol;
    }
}
