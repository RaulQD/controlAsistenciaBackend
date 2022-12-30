package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.serializer.Deserializer;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name="tb_area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArea;
    private String nombre;

}
