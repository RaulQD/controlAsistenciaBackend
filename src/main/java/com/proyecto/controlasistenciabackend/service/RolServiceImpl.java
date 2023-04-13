package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Rol;
import com.proyecto.controlasistenciabackend.entity.enums.RolNombre;
import com.proyecto.controlasistenciabackend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements RolService {

    @Autowired
    RolRepository rolRepository;
    @Override
    public Optional<Rol> getByRolNombre(RolNombre rolNombre) {
        return rolRepository.findByRolNombre(rolNombre);
    }
}
