package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Rol;
import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.service.RolService;
import com.proyecto.controlasistenciabackend.service.UsuarioService;
import com.proyecto.controlasistenciabackend.util.AppSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UsuarioController {

    private static Logger log = LoggerFactory.getLogger(UsuarioController.class);
        @Autowired
        UsuarioService usuarioService;
        @Autowired
        RolService rolService;
        @Autowired
        PasswordEncoder passwordEncoder;

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos(){
        List<Usuario> salida = usuarioService.listarEmpleados();
        return ResponseEntity.ok(salida);
    }
    // Listar todos los empleados paginados
    @GetMapping("/empleados/page")
    public ResponseEntity<Page<Usuario>> listarEmpleadosPaginados(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "idEmpleado") String order)
    {
        Page<Usuario> empleado = usuarioService.paginas(PageRequest.of(page, size, Sort.by(order)));

        return new ResponseEntity<Page<Usuario>>(empleado, HttpStatus.OK);
    }

    // Listar todos los empleados POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarEmpleado(@PathVariable("id") int idEmpleado) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<Usuario> objEmpleado = usuarioService.buscarEmpleadoPorId(idEmpleado);
        if (!objEmpleado.isPresent()) {
            response.put("mensaje", "El empleado con el ID: " + idEmpleado + " no existe en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("mensaje", "Empleado encontrado");
        Usuario usuario = objEmpleado.get();
        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }
    //BUSCAR POR NOMBRE
//    @GetMapping("/buscarNombre/{nombre}")
//    public ResponseEntity<List<Empleado>> buscarPorNombre(@PathVariable("nombre") String nombre) {
//        List<Empleado> lista = null;
//        try{
//            if(nombre.equals("todos")){
//                lista = empleadoService.listarPorNombre("%");
//            }else{
//                lista = empleadoService.listarPorNombre("%" + nombre + "%");
//            }
//        }catch(Exception e){
//            log.error("Error al buscar por nombre: " + e.getMessage());
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(lista);
//    }
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<HashMap<String, Object>> listarPorNombre(@RequestParam(name = "nombre", required = false,defaultValue = "") String nombre){
        HashMap<String, Object> response = new HashMap<>();
        try{
            List<Usuario> lista = usuarioService.listarPorNombre("%"+nombre+"%");
            if (CollectionUtils.isEmpty(lista)) {
                response.put("mensaje", "No se encontraron empleados con el nombre: " + nombre);
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else {
                response.put("empleado", lista);
                response.put("mensaje", "Existen " + lista.size() + " registros para mostrar");
                return  new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
            response.put("mensaje", "Error al listar empleados por nombre");
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //REGISTRAR EMPLEADO
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        HashMap<String, Object> salida = new HashMap<>();

        try {
            Optional<Usuario> objDni = usuarioService.buscarPorDni(usuario.getDni());
            if (objDni.isEmpty()) {
                 Usuario objUser = usuarioService.findByUsuario(usuario.getUsuario());
                if (objUser == null) {
                        usuario.setFechaRegistro(new Date());
                        usuario.setEstado("Activo");
                        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
                        Set<Rol> roles = new HashSet<>();
                        roles.add(rolService.findByRoles("ROL_USER").get());
                        if (usuario.getRoles().contains("admin")) {
                            roles.add(rolService.findByRoles("ROL_ADMIN").get());
                        }
                        usuario.setRoles(roles);
                        Usuario objUsuario = usuarioService.insertarUsuario(usuario);
                        if (objUsuario == null) {
                            salida.put("mensaje", "Error al registrar el empleado");
                            return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.BAD_REQUEST);

                        } else {
                            salida.put("mensaje", "El empleado  con el ID: " + usuario.getIdUsuario() + " ha sido registrado con éxito");
                            salida.put("empleado", usuario);
                            return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CREATED);
                        }
                } else {
                    salida.put("mensaje", "El usuario: " + usuario.getUsuario() + " ya existe.");
                    return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CONFLICT);
                }
            } else {
                salida.put("mensaje", "El empleado con el DNI: " + usuario.getDni() + " ya existe.");
                return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CONFLICT);
            }
        } catch (DataAccessException e) {
            salida.put("mensaje", "Error al registrar el empleado");
            salida.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//        try {
//            Usuario obj = usuarioService.buscarPorDni(usuario.getDni());
//            if (obj != null) {
//                salida.put("mensaje", "El empleado con el DNI: " + usuario.getDni() + " ya existe.");
//                return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CONFLICT);
//            } else {
//                    usuario.setFechaRegistro(new Date());
//                    usuario.setEstado("Activo");
//                    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
//                    Set<Rol> roles = new HashSet<>();
//                    roles.add(rolService.findByRoles("ROL_USER").get());
//                    if(usuario.getRoles().contains("admin")){
//                        roles.add(rolService.findByRoles("ROL_ADMIN").get());
//                    }
//                    usuario.setRoles(roles);
//                    Usuario objUsuario = usuarioService.insertarUsuario(usuario);
//                if(objUsuario != null ){
//                    salida.put("mensaje", "El empleado  con el ID: " + usuario.getIdUsuario() + " ha sido registrado con éxito");
//                    salida.put("empleado", usuario);
//                    return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CREATED);
//                }else {
//                    salida.put("mensaje", "Error al registrar el empleado");
//                    return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.BAD_REQUEST);
//                }
//            }
//        } catch (DataAccessException e) {
//            salida.put("mensaje", "Error al registrar el empleado");
//            salida.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
//            return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
     //ACTUALIZAR EMPLEADO
    @PutMapping("/actualizar/{id}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> actualizar(@RequestBody Usuario usuario) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Optional<Usuario> obj = usuarioService.buscarEmpleadoPorId(usuario.getIdUsuario());
            if (obj.isPresent()) {
                Usuario objSalida = usuarioService.actualizarEmpleado(usuario);
                if (objSalida == null) {
                    response.put("mensaje", "No se actualizó correctamente");
                    return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                } else {
                    response.put("mensaje","Se actualizó correctamente el ID " + usuario.getIdUsuario());
                    return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
                }
            } else {
                response.put("mensaje","No se encontró el ID " + usuario.getIdUsuario());
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje","Error al actualizar " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
        //ELIMINAR EMPLEADO

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
