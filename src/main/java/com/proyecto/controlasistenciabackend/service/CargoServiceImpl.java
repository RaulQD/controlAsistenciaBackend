package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Cargo;
import com.proyecto.controlasistenciabackend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoServiceImpl implements CargoService{

    @Autowired
    CargoRepository cargoRepository;

    @Override
    public List<Cargo> listarTodos() {
        return  cargoRepository.findAll();
    }
}
