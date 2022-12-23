package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.entity.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AreaService {

    public abstract List<Area> listarTodos();
    public abstract Optional<Area> buscarAreaPorId(int idArea);
    public Page<Area> paginas(Pageable pageable);
    public abstract Area insertarArea(Area area);
    public abstract Area actualizarArea(Area area);
    public abstract Optional<Area> buscarArea(int idArea);
    public abstract void eliminarArea(int idArea);
}
