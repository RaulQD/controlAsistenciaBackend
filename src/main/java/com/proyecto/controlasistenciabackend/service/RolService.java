package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.ERol;
import com.proyecto.controlasistenciabackend.entity.Rol;

import java.util.Optional;

public interface RolService {

    public Optional<Rol> findByNombre_rol(ERol nombre_rol);
}
