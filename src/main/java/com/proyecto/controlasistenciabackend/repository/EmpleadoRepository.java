package com.proyecto.controlasistenciabackend.repository;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    public abstract Empleado findByDni(String dni);
//    @Query("select e from Empleado e where e.dni =?1 and e.idEmpleado <> ?2")
//    public abstract List<Empleado> listarPorDniDiferenteDeSiMismo(String dni, int idEmpleado);
    @Query("select e from Empleado e where e.nombre like ?1 ")
    public abstract List<Empleado> findByNombre(String nombre);

}
