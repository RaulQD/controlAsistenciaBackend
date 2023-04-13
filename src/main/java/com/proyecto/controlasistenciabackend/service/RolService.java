package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Rol;
import com.proyecto.controlasistenciabackend.entity.enums.RolNombre;

import java.util.Optional;

public interface RolService {

    public Optional<Rol> getByRolNombre(RolNombre rolNombre);
}
