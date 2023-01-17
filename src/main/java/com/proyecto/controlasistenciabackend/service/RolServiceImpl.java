package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Rol;
import com.proyecto.controlasistenciabackend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class RolServiceImpl implements RolService{

    @Autowired
    RolRepository rolRepository;
    @Override
    public List<Rol> listarRoles() {
         return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findByRoles(String roles) {
        return rolRepository.findByRoles(roles);
    }


}
