package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService{

    @Autowired
    AreaRepository areaRepository;

    @Override
    public List<Area> listarTodos() {
        return  areaRepository.findAll();
    }

    @Override
    public Area insertarArea(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area actualizarArea(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Optional<Area> buscarArea(int idArea) {
        return areaRepository.findById(idArea);
    }

    @Override
    public void eliminarArea(int idArea) {
        areaRepository.deleteById(idArea);
    }
}
