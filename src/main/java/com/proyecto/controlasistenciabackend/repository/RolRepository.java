package com.proyecto.controlasistenciabackend.repository;

import com.proyecto.controlasistenciabackend.entity.ERol;
import com.proyecto.controlasistenciabackend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByNombre_rol(ERol nombre_rol);
}
