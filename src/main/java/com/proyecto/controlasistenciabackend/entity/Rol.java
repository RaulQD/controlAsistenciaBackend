package com.proyecto.controlasistenciabackend.entity;

import com.proyecto.controlasistenciabackend.entity.enums.RolNombre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;

    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol() {

    }

    public Rol(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }
}
