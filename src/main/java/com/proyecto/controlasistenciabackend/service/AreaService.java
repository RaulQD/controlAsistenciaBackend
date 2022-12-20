package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Area;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface AreaService {

    public abstract List<Area> listarTodos();

    public abstract Area insertarArea(Area area);
    public abstract Area actualizarArea(Area area);
    public abstract Optional<Area> buscarArea(int idArea);
    public abstract void eliminarArea(int idArea);
}
