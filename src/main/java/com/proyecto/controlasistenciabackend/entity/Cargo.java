package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_cargo")
@Getter
@Setter
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCargo;
    private String nombre;

}
