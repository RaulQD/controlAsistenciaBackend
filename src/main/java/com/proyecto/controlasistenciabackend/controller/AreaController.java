package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.service.AreaService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/area")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AreaController {
    @Autowired
    AreaService areaService;


    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registra(@Valid @RequestBody Area area, Errors errors){
        HashMap<String, Object> response = new HashMap<>();
        List<String> listaMensaje = new ArrayList<>();
        response.put("errores",listaMensaje);

        List<ObjectError> lstError = errors.getAllErrors();
        for(ObjectError objectError: lstError){
            objectError.getDefaultMessage();
            listaMensaje.add(objectError.getDefaultMessage());
        }
        if(!CollectionUtils.isEmpty(listaMensaje)){
            response.put("errores", listaMensaje);
            return ResponseEntity.ok(response);
        }
        try{
            Area objArea = areaService.insertarArea(area);
            if(objArea == null){
                listaMensaje.add("No se registro Correctamente");
            }else{
                listaMensaje.add("Se registro Correctamente el Area " + objArea.getNombre());
            }
        }catch(Exception e){
            e.printStackTrace();
            listaMensaje.add("Error al registrar");
        }
        return ResponseEntity.ok(response);
    }
}
