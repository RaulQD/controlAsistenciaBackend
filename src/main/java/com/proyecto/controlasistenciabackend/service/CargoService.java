package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CargoService {

   public abstract List<Cargo> findAllCargo();
    public abstract Optional<Cargo> buscarPorId(int idCargo);
    public abstract Cargo insertarCargo(Cargo cargo);
    public abstract Cargo actualizarCargo(Cargo cargo);
    public abstract  void  eliminarCargo(int idCargo);

    public Page<Cargo> listarPorPagina(Pageable pageable);
}
