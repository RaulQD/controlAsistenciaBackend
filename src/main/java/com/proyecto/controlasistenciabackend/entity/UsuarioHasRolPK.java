package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UsuarioHasRolPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id_usuario;
    private int id_rol;

}