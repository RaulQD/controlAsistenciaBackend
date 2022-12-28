package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Cargo;
import com.proyecto.controlasistenciabackend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoServiceImpl implements CargoService{

    @Autowired
    CargoRepository cargoRepository;

    @Override
    public List<Cargo> listarTodos() {
        return  cargoRepository.findAll();
    }

    @Override
    public Optional<Cargo> buscarPorId(int idCargo) {
        return Optional.empty();
    }

    @Override
    public Cargo insertarCargo(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Override
    public Cargo actualizarCargo(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Override
    public void  eliminarCargo(int idCargo) {
        cargoRepository.deleteById(idCargo);
    }

    @Override
    public Page<Cargo> listarPorPagina(Pageable pageable) {
        return cargoRepository.findAll(pageable);
    }

}
