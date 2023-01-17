package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    public abstract List<Rol> listarRoles();

    public Optional<Rol> findByRoles(String roles);
}
