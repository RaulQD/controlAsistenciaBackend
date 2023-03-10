package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.repository.AreaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService{

    @Autowired
    AreaRepository areaRepository;


    @Override
    public List<Area> findAllAreas() {
        return areaRepository.findAllAreas();
    }

    @Override
    public Optional<Area> buscarAreaPorId(int idArea) {
        return areaRepository.findById(idArea);
    }

    @Override
    public Page<Area> paginas(Pageable pageable) {
        return areaRepository.findAll(pageable);
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
