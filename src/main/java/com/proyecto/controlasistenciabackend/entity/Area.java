package com.proyecto.controlasistenciabackend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name="area")
public class Area implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArea;
    private String area;


}
