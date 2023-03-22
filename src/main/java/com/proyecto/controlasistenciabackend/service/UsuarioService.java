package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsuarioService {

    //TODO:LISTADORES
    public abstract List<Usuario> listarEmpleados();

    //TODO:BUSCADORES
    public abstract List<Usuario> listarPorNombre(String nombre);
    public abstract Optional<Usuario> buscarPorDni(String dni);

    //TODO:INSERTADORES
    public Usuario insertarUsuario(Usuario usuario);
    public abstract Usuario actualizarEmpleado(Usuario usuario);
    public abstract void eliminarEmpleado(int idUsuario);

    //TODO:PAGINADOR
    public Page<Usuario> paginas(Pageable pageable);


    //TODO:VALIDACIONES
    public abstract Usuario buscarEmpleadoPorId(int idUsuario);

    //TODO:EXPORTAR E IMPORTAR
    public abstract ByteArrayInputStream exportarUsuarioExcel(List<Usuario> lstUsuarios);
    public abstract Map<String, Object> importarUsuarioExcel(MultipartFile file);

}
