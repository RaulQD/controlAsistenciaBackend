package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;
    private String roles;

    public Rol() {
    }

    public Rol(int idRol, String roles) {
        this.idRol = idRol;
        this.roles = roles;
    }
}
