package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import com.proyecto.controlasistenciabackend.service.EmpleadoService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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

    import java.util.*;

    @RestController
    @RequestMapping("/api/empleado")
    @CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EmpleadoController {

        @Autowired
        EmpleadoService empleadoService;

        @GetMapping("/empleado")
        public ResponseEntity<Page<Empleado>> paginas(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "6") int size,
                                                      @RequestParam(defaultValue = "idEmpleado") String order,
                                                      @RequestParam(defaultValue = "true") boolean asc
        ) {
            Page<Empleado> empleado = empleadoService.paginas(PageRequest.of(page, size, Sort.by(order)));
            if (!asc) {
                empleado = empleadoService.paginas(PageRequest.of(page, size, Sort.by(order).descending()));
            }
            return new ResponseEntity<Page<Empleado>>(empleado, HttpStatus.OK);
        }

        @GetMapping("/buscar/{id}")
        public ResponseEntity<Empleado> buscarEmpleado(@PathVariable("id") int idEmpleado) {
           Optional<Empleado> objEmpleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
            if(!objEmpleado.isPresent()){
                return ResponseEntity.notFound().build();
            }
            Empleado empleado = objEmpleado.get();
            return ResponseEntity.ok(empleado);
        }
//    @GetMapping("/listar")
//    public ResponseEntity<List<Empleado>> listarTodos(){
//        List<Empleado> lista = empleadoService.listarTodos();
//        return ResponseEntity.ok(lista);
//    }

        @PostMapping("/registrar")
        @ResponseBody
        public ResponseEntity<?> registrar(@Valid @RequestBody Empleado objEmpleado, Errors errors) {
            HashMap<String, Object> response = new HashMap<>();
            List<String> listaMensaje = new ArrayList<>();
            response.put("errores", listaMensaje);

            List<ObjectError> lstError = errors.getAllErrors();
            for (ObjectError objectError : lstError) {
                objectError.getDefaultMessage();
                listaMensaje.add(objectError.getDefaultMessage());
            }
            if (!CollectionUtils.isEmpty(listaMensaje)) {
                response.put("errores", listaMensaje);
                return ResponseEntity.ok(response);
            }
            try {
                Empleado obj = empleadoService.buscarPorDni(objEmpleado.getDni());
                if (obj != null) {
                    listaMensaje.add("El DNI: " + obj.getDni() + " ya existe");
                } else {
                    objEmpleado.setFechaRegistro(new Date());
                    objEmpleado.setEstado(1);
                    objEmpleado.setIdEmpleado(0);
                    Empleado objSalida = empleadoService.insertarEmpleado(objEmpleado);
                    if (objSalida == null) {
                        listaMensaje.add("No se registró correctamente");
                    } else {
                        listaMensaje.add("Se registró correctamente el ID " + objSalida.getIdEmpleado());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                listaMensaje.add("Error al registrar");
            }
            return ResponseEntity.ok(response);
        }

        @PutMapping("/actualizar/{id}")
        @ResponseBody
        public ResponseEntity<Empleado> actualizar(@Valid @RequestBody Empleado empleado, @PathVariable("id") int idEmpleado) {
            HashMap<String, Object> response = new HashMap<>();
            try {
                Optional<Empleado> obj = empleadoService.buscarEmpleadoPorId(idEmpleado);
                if (obj.isPresent()) {
                    Empleado objSalida = empleadoService.actualizarEmpleado(empleado);
                    if (objSalida == null) {
                        response.put("mensaje","No se actualizó correctamente");
                    } else {
                        response.put("mensaje","Se actualizó correctamente el ID " + idEmpleado);
                    }
                } else {
                    response.put("mensaje","No se encontró el ID " + idEmpleado);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("mensaje","Error al actualizar");
            }
            return ResponseEntity.ok(empleado);
        }
        }
