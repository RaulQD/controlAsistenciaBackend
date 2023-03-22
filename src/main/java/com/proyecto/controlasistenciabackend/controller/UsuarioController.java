package com.proyecto.controlasistenciabackend.controller;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.service.UsuarioService;
import com.proyecto.controlasistenciabackend.util.AppSettings;

import com.proyecto.controlasistenciabackend.util.Constantes;
import com.proyecto.controlasistenciabackend.util.FunctionUtil;
import org.apache.coyote.Response;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.*;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UsuarioController {

    private static Logger log = LoggerFactory.getLogger(UsuarioController.class);
        @Autowired
        UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos(){
        List<Usuario> salida = usuarioService.listarEmpleados();
        return ResponseEntity.ok(salida);
    }

    //LISTAR TODOS LOS EMPLEADOS PAGINADOS
    @GetMapping("/empleados/page/{page}")
    public Page<Usuario> listarEmpleadosPaginados(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 8, Sort.by("idUsuario"));
        return usuarioService.paginas(pageable);
    }

    // Listar todos los empleados POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarEmpleado(@PathVariable("id") int idEmpleado) {
        HashMap<String, Object> response = new HashMap<>();
        try{
            Usuario objusuario = usuarioService.buscarEmpleadoPorId(idEmpleado);
            if(objusuario == null){
                response.put("mensaje","El empleado con el ID: " + idEmpleado + " no existe en la base de datos");
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                return new ResponseEntity<Usuario>(objusuario, HttpStatus.OK);
            }
        }catch(DataAccessException e){
            response.put("mensaje","Error al realizar la consulta en la base de datos");
            response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //BUSCAR POR NOMBRE
//    @GetMapping("/buscarNombre/{nombre}")
//    public ResponseEntity<List<Usuario>> buscarPorNombre(@PathVariable("nombre") String nombre) {
//        HashMap<String,Object> response = new HashMap<>();
//        try{
//            List<Usuario> lista = usuarioService.listarPorNombre("%"+nombre+"%");
//        if(CollectionUtils.isEmpty(lista)){
//            response.put("mensaje","No se encontraron empleados con el nombre: " + nombre);
//            return new ResponseEntity<List<Usuario>>(HttpStatus.NOT_FOUND);
//        }else{
//            response.put("mensaje","Se encontraron " + lista.size() + " empleados con el nombre: " + nombre + " en la base de datos");
//            response.put("empleado",lista);
//            return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);
//        }
//        }catch(Exception e){
//            e.printStackTrace();
//            response.put("mensaje","Error al listar empleados por nombre");
//            return new ResponseEntity<List<Usuario>>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
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
                response.put("mensaje", "Existe " + lista.size() + " registros para mostrar");
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
                    usuario.setFechaRegistro(new Date());
                    usuario.setEstado("Activo");
                    usuario.setIdUsuario(0);
//                    usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
//                    Set<Rol> roles = new HashSet<>();
//                    roles.add(rolService.findByRolName(RolName.ROLE_USER).get());
//                    if (usuario.getRoles().contains("admin")) {
//                        roles.add(rolService.findByRolName(RolName.ROLE_ADMIN).get());
//                    }
//                    usuario.setRoles(roles);
                    Usuario objUsuario = usuarioService.insertarUsuario(usuario);
                    if (objUsuario != null) {
                        salida.put("mensaje", "El empleado  con el ID: " + usuario.getIdUsuario() + " ha sido registrado con éxito");
                        salida.put("empleado", usuario);
                        return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.CREATED);
                    } else {
                        salida.put("mensaje", "Error al registrar el empleado");
                        return new ResponseEntity<HashMap<String, Object>>(salida, HttpStatus.BAD_REQUEST);
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
     //ACTUALIZAR EMPLEADO
    @PutMapping("/actualizar/{id}")
    @ResponseBody
    public ResponseEntity<?> actualizar(@RequestBody Usuario usuario, @PathVariable Integer id){
        HashMap<String, Object> response = new HashMap<>();
        try {
            Usuario obj = usuarioService.buscarEmpleadoPorId(usuario.getIdUsuario());
            if(obj != null){
                Usuario objSalida = usuarioService.actualizarEmpleado(usuario);
                if(objSalida != null) {
                    response.put("mensaje", "El empleado con el ID: " + usuario.getIdUsuario() + " ha sido actualizado con éxito");
                    response.put("empleado", usuario);
                    return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
                }else{
                    response.put("mensaje","No se actualizó correctamente");
                    return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }else{
                response.put("mensaje","El empleado con el ID: " + usuario.getIdUsuario() + " no existe");
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje","Error al actualizar " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
        //ELIMINAR EMPLEADO
    @DeleteMapping("/eliminar/{idUsuario}")
    @ResponseBody
    public ResponseEntity<?> eliminar(@PathVariable int idUsuario){
        HashMap<String, Object> response = new HashMap<>();
        try {
            Usuario obj = usuarioService.buscarEmpleadoPorId(idUsuario);
            if(obj != null){
                usuarioService.eliminarEmpleado(idUsuario);
                response.put("mensaje", "El empleado con el ID: " + idUsuario + " ha sido eliminado con éxito");
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.OK);
            }else{
                response.put("mensaje","El empleado con el ID: " + idUsuario + " no existe");
                return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje","Error al eliminar " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    //DESCARGAR ARCHIVO EXCEL
    @GetMapping("/descargarExcel")
    public ResponseEntity<Resource> listaDescarga() {

        log.info("Descargando archivo excel");
        ByteArrayInputStream bytes = null;
        InputStreamResource body = null;
        String filename = null;
        try {
            List<Usuario> lista = usuarioService.listarEmpleados();
            filename = "Planilla_listado_empleado__" + FunctionUtil.getDateNowInString() + ".xlsx";
            bytes = usuarioService.exportarUsuarioExcel(lista);
            body = new InputStreamResource(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.SET_COOKIE, "fileDownload=true; path=/")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.available()))
                .contentType(MediaType.parseMediaType(Constantes.TYPE_EXCEL))
                .body(body);
    }
    @PostMapping("/importExcel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file){
        Map<String, Object> response = usuarioService.importarUsuarioExcel(file);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
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
