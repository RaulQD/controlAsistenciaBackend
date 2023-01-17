package com.proyecto.controlasistenciabackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario_has_rol")
public class UsuarioHasRol {

    @EmbeddedId
    private UsuarioHasRolPK usuarioHasRolPk;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false, insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idRol", nullable = false, insertable = false, updatable = false)
    private Rol rol;

}