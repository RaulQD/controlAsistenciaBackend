package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.repository.AreaRepository;
import com.proyecto.controlasistenciabackend.repository.CargoRepository;
import com.proyecto.controlasistenciabackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.AttributeInUseException;
import javax.print.AttributeException;
import java.util.*;


@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listarEmpleados() {
        return usuarioRepository.findAll();
    }


    @Override
    public List<Usuario> listarPorNombre(String nombre) {
        return usuarioRepository.listarPorNombre(nombre);
    }

    @Override
    public Usuario actualizarEmpleado(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    @Override
    public Usuario insertarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarEmpleado(int idEmpleado) {
        usuarioRepository.deleteById(idEmpleado);
    }

    @Override
    public Optional<Usuario> buscarEmpleadoPorId(int idEmpleado) {
        return usuarioRepository.findById(idEmpleado);
    }

    @Override
    public Usuario findByUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }


    @Override
    public Optional<Usuario> buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> paginas(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
}
