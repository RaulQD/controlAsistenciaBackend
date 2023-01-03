package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import com.proyecto.controlasistenciabackend.service.EmpleadoService;
import com.proyecto.controlasistenciabackend.util.AppSettings;

import jakarta.validation.Valid;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class EmpleadoController {

    private static Logger log = LoggerFactory.getLogger(EmpleadoController.class);
    @Autowired
    EmpleadoService empleadoService;

    // Listar todos los empleados paginados
    @GetMapping("/empleado")
    public ResponseEntity<Page<Empleado>> paginas(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "6") int size,
                                                  @RequestParam(defaultValue = "idEmpleado") String order,
                                                  @RequestParam(defaultValue = "true") boolean asc
    ) {
        Page<Empleado> empleado = empleadoService.paginas(PageRequest.of(page, size, Sort.by(order)));
        if (!asc) {
            empleadoService.paginas(PageRequest.of(page, size, Sort.by(order).descending()));
        }
        return new ResponseEntity<Page<Empleado>>(empleado, HttpStatus.OK);
    }

    // Listar todos los empleados POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarEmpleado(@PathVariable("id") int idEmpleado) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<Empleado> objEmpleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
        if (!objEmpleado.isPresent()) {
            response.put("mensaje", "El empleado con el ID: " + idEmpleado + " no existe en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("mensaje", "Empleado encontrado");
        Empleado empleado = objEmpleado.get();
        return ResponseEntity.status(HttpStatus.OK).body(empleado);
    }
//    @GetMapping("/buscar/{id}")
//    public ResponseEntity<?> buscarEmpleado(@PathVariable("id") int idEmpleado) {
//        HashMap<String, Object> response = new HashMap<>();
//        Optional<Empleado> objEmpleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
//        if (!objEmpleado.isPresent()) {
//            response.put("mensaje", "El empleado con el ID: " + idEmpleado + " no existe en la base de datos");
//            return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
//        }else{
//            response.put("mensaje", "Empleado encontrado");
//            response.put("empleado", objEmpleado);
//            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
//        }
//    }
    //BUSCAR POR NOMBRE
    @GetMapping("/buscarPorNombre/{nombre}")
    public ResponseEntity<HashMap<String, Object>> listarPorNombre(@RequestParam(name = "nombre", required = false,defaultValue = "") String nombre){
        HashMap<String, Object> response = new HashMap<>();
        List<Empleado> lista = empleadoService.listarPorNombre("%"+nombre+"%");
        if (CollectionUtils.isEmpty(lista)) {
            response.put("mensaje", "No se encontraron empleados con el nombre: " + nombre);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(lista.size() == 1) {
            response.put("mensaje", "Se encontró 1 empleado con el nombre: " + nombre);
        }else{
            response.put("mensaje", "Se encontraron " + lista.size() + " empleados con el nombre: " + nombre);
        }
        response.put("empleados", lista);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    //REGISTRAR EMPLEADO
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEmpleado(@RequestBody Empleado empleado) {
        HashMap<String, Object> salida = new HashMap<>();
        try {
            Empleado obj = empleadoService.buscarPorDni(empleado.getDni());
            if (obj != null) {
                salida.put("mensaje", "El empleado con el DNI: " + empleado.getDni() + " ya existe.");
                return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CONFLICT);
            } else {
                empleado.setFechaRegistro(new Date());
                empleado.setEstado(1);
                empleado.setIdEmpleado(0);
                Empleado objEmpleado = empleadoService.insertarEmpleado(empleado);
                if (objEmpleado == null) {
                    salida.put("mensaje", "Error al registrar el empleado");
                    return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    salida.put("mensaje", "El empleado  con el ID: " + empleado.getIdEmpleado() +" ha sido registrado con éxito");
                    salida.put("empleado", objEmpleado);
                    return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CREATED);
                }
            }
        } catch (DataAccessException e) {
            salida.put("mensaje", "Error al registrar el empleado");
            salida.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     //ACTUALIZAR EMPLEADO
        @PutMapping("/actualizar/{id}")
        @ResponseBody
        public ResponseEntity<HashMap<String, Object>> actualizar(@RequestBody Empleado empleado) {
            HashMap<String, Object> response = new HashMap<>();
            try {
                Optional<Empleado> obj = empleadoService.buscarEmpleadoPorId(empleado.getIdEmpleado());
                if (obj.isPresent()) {
                    Empleado objSalida = empleadoService.actualizarEmpleado(empleado);
                    if (objSalida == null) {
                        response.put("mensaje", "No se actualizó correctamente");
                        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                    } else {
                        response.put("mensaje","Se actualizó correctamente el ID " + empleado.getIdEmpleado());
                        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
                    }
                } else {
                    response.put("mensaje","No se encontró el ID " + empleado.getIdEmpleado());
                    return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.put("mensaje","Error al actualizar " + e.getMessage());
            }
            return ResponseEntity.ok(response);
        }
        //ELIMINAR EMPLEADO
        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<HashMap<String,Object>> eliminar(@PathVariable("id") int idEmpleado){
            HashMap<String,Object> response = new HashMap<>();
            try{
                Optional<Empleado> objEmpleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
                if(objEmpleado.isPresent()){
                    String nombreFotoAnteriro = objEmpleado.get().getFoto();
                    if(nombreFotoAnteriro != null && nombreFotoAnteriro.length() > 0){
                        Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnteriro).toAbsolutePath();
                        File archivoFotoAnterior = rutaFotoAnterior.toFile();
                        if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()){
                            archivoFotoAnterior.delete();
                        }
                    }
                  empleadoService.eliminarEmpleado(idEmpleado);
                  response.put("mensaje","Se eliminó correctamente");
                }else{
                    response.put("mensaje","No se encontró el ID "+ idEmpleado);
                    return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.NOT_FOUND);
                }
            }catch(Exception e){
                e.printStackTrace();
                response.put("mensaje","Error al eliminar "+ e.getMessage());
                return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.ok(response);
        }
//        @PostMapping("/upload")
//        public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") int idEmpleado){
//        HashMap<String,Object> response = new HashMap<>();
//              Empleado objEmpleado = empleadoService.buscarEmpleadoPorId(idEmpleado).orElse(null);
//            if(!archivo.isEmpty()){
//                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
//                Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
//                log.info(rutaArchivo.toString());
//                try{
//                    Files.copy(archivo.getInputStream(), rutaArchivo);
//                }catch(IOException e){
//                    response.put("mensaje","Error al subir la imagen "+ nombreArchivo);
//                    response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
//                    return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//                String nombreFotoAnterior = objEmpleado.getFoto();
//                if(nombreFotoAnterior != null && nombreFotoAnterior.length() > 0){
//                    Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFotoAnterior).toAbsolutePath();
//                    File archivoFotoAnterior = rutaFotoAnterior.toFile();
//                    if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()){
//                        archivoFotoAnterior.delete();
//                    }
//                }
//                objEmpleado.setFoto(nombreArchivo);
//                empleadoService.insertarEmpleado(objEmpleado);
//                response.put("empleado",objEmpleado);
//                response.put("mensaje","Has subido correctamente la imagen "+ nombreArchivo);
//            }
//            return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.CREATED);
//        }
//        @GetMapping("/uploads/img/{nombreFoto:.+}")
//        public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
//            Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
//            log.info(rutaArchivo.toString());
//
//            Resource recurso = null;
//            try{
//                recurso = new UrlResource(rutaArchivo.toUri());
//            }catch(MalformedURLException e){
//                e.printStackTrace();
//            }
//            if(!recurso.exists() && !recurso.isReadable()){
//                rutaArchivo = Paths.get("src/main/resources/static/images").resolve("user.png").toAbsolutePath();
//                try{
//                    recurso = new UrlResource(rutaArchivo.toUri());
//                }catch(MalformedURLException e){
//                    e.printStackTrace();
//                }
//                log.error("Error no se puede cargar la imagen "+ nombreFoto);
//            }
//            HttpHeaders cabecera = new HttpHeaders();
//            cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
//            return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
//        }
    }
