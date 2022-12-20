package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Empleado;
import com.proyecto.controlasistenciabackend.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Override
    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado insertarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado actualizarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Optional<Empleado> buscarEmpleadoPorId(int idEmpleado) {
        return empleadoRepository.findById(idEmpleado);
    }


    @Override
    public Empleado buscarPorDni(String dni) {
        return empleadoRepository.findByDni(dni);
    }

    @Override
    public void eliminarEmpleado(int idEmpleado) {
        empleadoRepository.deleteById(idEmpleado);
    }

    @Override
    public Page<Empleado> paginas(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

}
