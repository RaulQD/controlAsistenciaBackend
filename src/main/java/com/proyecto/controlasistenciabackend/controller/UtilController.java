package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.entity.Cargo;
import com.proyecto.controlasistenciabackend.service.AreaService;
import com.proyecto.controlasistenciabackend.service.CargoService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UtilController {
    @Autowired
    AreaService areaService;

    @Autowired
    CargoService cargoService;


    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listarArea(){
        List<Area> list = areaService.listarTodos();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/cargos")
    public ResponseEntity<List<Cargo>> listarCargo(){
        List<Cargo> list = cargoService.listarTodos();
        return ResponseEntity.ok(list);
    }
}
