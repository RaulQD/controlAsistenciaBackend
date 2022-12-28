package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Cargo;
import com.proyecto.controlasistenciabackend.service.CargoService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cargo")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class CargoController {

    @Autowired
    CargoService cargoService;

    @GetMapping("/Buscar/{id}")
    public ResponseEntity<Cargo> listarPorId(int idCargo){
        Optional<Cargo> objCargo = cargoService.buscarPorId(idCargo);
        if(objCargo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cargo cargo = objCargo.get();
        return ResponseEntity.ok(cargo);
    }
}
