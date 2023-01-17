package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Area;
import com.proyecto.controlasistenciabackend.service.AreaService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/listar")
    public ResponseEntity<List<Area>> listarAreas(){
        List<Area> salida = areaService.listarTodos();
        return ResponseEntity.ok(salida);
    }
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Area> buscarArea(@PathVariable("id") int idArea){
        Optional<Area> objArea = areaService.buscarAreaPorId(idArea);
        if(!objArea.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Area area = objArea.get();
        return  ResponseEntity.ok(area);
    }
    @GetMapping("/area")
    public ResponseEntity<Page<Area>> paginas(  @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "4") int size,
                                                @RequestParam(defaultValue = "idArea") String order,
                                                @RequestParam(defaultValue = "true") boolean asc)
    {
        Page<Area> area = areaService.paginas(PageRequest.of(page,size, Sort.by(order)));
        if(!asc){
          areaService.paginas(PageRequest.of(page,size, Sort.by(order).descending()));
        }
        return new ResponseEntity<Page<Area>>(area, HttpStatus.OK);
    }
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
            area.setIdArea(0);
            Area objArea = areaService.insertarArea(area);
            if(objArea == null){
                listaMensaje.add("No se registro correctamente");
            }else{
                listaMensaje.add("Se registro correctamente el ID " + objArea.getIdArea());
            }
        }catch(Exception e){
            e.printStackTrace();
            listaMensaje.add("Error al registrar");
        }
        return ResponseEntity.ok(response);
    }
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Area area) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<Area> objArea = areaService.buscarAreaPorId(area.getIdArea());
            if (objArea.isPresent()) {
                Area objSalida = areaService.actualizarArea(area);
                if (objSalida == null) {
                    response.put("mensaje", "No se actualizo correctamente");
                } else {
                    response.put("mensaje", "Se actualizo correctamente el ID " + objSalida.getIdArea());
                }
            } else {
                response.put("mensaje", "No se encontro el ID " + area.getIdArea());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al actualizar");
        }
        return ResponseEntity.ok(response);
    }

}
