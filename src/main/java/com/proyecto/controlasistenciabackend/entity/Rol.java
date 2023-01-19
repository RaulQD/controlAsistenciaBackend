package com.proyecto.controlasistenciabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;



@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
public class Rol{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRol;
    private String roles;

    public Rol(String roles) {
        this.roles = roles;
    }
}
