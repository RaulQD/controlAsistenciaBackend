package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    public abstract List<Empleado> listarTodos();
    public abstract Empleado insertarEmpleado(Empleado empleado);
    public abstract Empleado actualizarEmpleado(Empleado empleado);
    public abstract Optional<Empleado> buscarEmpleadoPorId(int idEmpleado);
    public abstract Empleado buscarPorDni(String dni);
    public abstract void eliminarEmpleado(int idEmpleado);
    public Page<Empleado> paginas(Pageable pageable);

}
