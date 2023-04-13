package com.proyecto.controlasistenciabackend.repository;

import com.proyecto.controlasistenciabackend.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    
    public abstract Optional<Usuario> findByDni(String dni);

    Optional<Usuario> findByUsuario(String usuario);
    @Query("select e from Usuario e where e.nombre like ?1 ")
    public List<Usuario> listarPorNombre(String nombre);

    @Query("select e from Usuario e where e.fechaRegistro between ?1 and ?2")
    public List<Usuario> listarPorFechaRegistro(String fechaInicio, String fechaFin);

}