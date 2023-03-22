package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.ERol;
import com.proyecto.controlasistenciabackend.entity.Rol;
import com.proyecto.controlasistenciabackend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    RolRepository rolRepository;
    @Override
    public Optional<Rol> findByNombre_rol(ERol nombre_rol) {
        return rolRepository.findByNombre_rol(nombre_rol);
    }
}
