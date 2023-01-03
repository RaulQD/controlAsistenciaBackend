package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmpleadoService {

    public abstract List<Empleado> listarTodos();
    public abstract Empleado insertarEmpleado(Empleado empleado);
    public abstract Empleado actualizarEmpleado(Empleado empleado);
    public abstract void eliminarEmpleado(int idEmpleado);
    public abstract Optional<Empleado> buscarEmpleadoPorId(int idEmpleado);
    public abstract Empleado buscarPorDni(String dni);
    public abstract List<Empleado> listarPorNombre(String nombre);
    public Page<Empleado> paginas(Pageable pageable);

    //TODO: Excel
//    public Map<String, Object> generarExcel(MultipartFile file);

//    public ByteArrayInputStream listarEmpleadosData(List<Empleado> lstEmpleado);

}
