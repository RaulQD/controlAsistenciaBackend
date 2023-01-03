package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Cargo;
import com.proyecto.controlasistenciabackend.entity.Empleado;
import com.proyecto.controlasistenciabackend.repository.AreaRepository;
import com.proyecto.controlasistenciabackend.repository.CargoRepository;
import com.proyecto.controlasistenciabackend.repository.EmpleadoRepository;
import com.proyecto.controlasistenciabackend.util.Constantes;
import com.proyecto.controlasistenciabackend.util.FunctionExcelUtil;
import com.proyecto.controlasistenciabackend.util.FunctionUtil;
import com.proyecto.controlasistenciabackend.util.UtilExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private CargoRepository cargoRepository;

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
    public List<Empleado> listarPorNombre(String nombre) {
        return empleadoRepository.findByNombre(nombre);
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
