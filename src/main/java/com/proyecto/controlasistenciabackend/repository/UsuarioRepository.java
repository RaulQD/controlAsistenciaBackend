package com.proyecto.controlasistenciabackend.repository;

import com.proyecto.controlasistenciabackend.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByUsuario(String usuario);
    boolean existsByContrasena(String contrasena);
    public abstract Usuario findByDni(String dni);

    @Query("select e from Usuario e where e.nombre like ?1 ")
    public List<Usuario> listarPorNombre(String nombre);

    public Usuario findByUsuario(String usuario);
}