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

    private static String[] COLUMNAS_tema = { "NOMBRES", "APELLIDOS","DNI", "CONTACTO", "CORREO",  "DIRECCIÓN", "FECHA NACIMIENTO","FECHA REGISTRO"};
    private static CellType[][] TIPO_DATOS_tema = {
            {CellType.STRING}, {CellType.STRING},{CellType.STRING, CellType.NUMERIC}, {CellType.STRING, CellType.NUMERIC}, {CellType.STRING}, {CellType.STRING}, {CellType.STRING}, {CellType.STRING}};
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    @Override
    public Map<String, Object> generarExcel(MultipartFile file) {
        Map<String, Object> mensajes = new HashMap<>();
        Workbook workbook = null;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            boolean noEsPrimero = false;
            mensajes = FunctionExcelUtil.validaCabecera(COLUMNAS_tema, sheet);
            if (mensajes.size() > 0) {
                return mensajes;
            }
            mensajes = FunctionExcelUtil.validaTipoDatosArreglo(TIPO_DATOS_tema, sheet);
            if (mensajes.size() > 0) {
                return mensajes;
            }
            List<Empleado> lstEmpleado = new ArrayList<>();
            String celdaNombre = null, celdaApellido = null, celdaDni = null,
                    celdaContacto = null, celdaCorreo = null, celdaDireccion = null,
                    celdaFechaNacimiento = null, celdaFechaRegistro = null, celdaAreaName = null,
                    celdaCargoName = null;

            for (Row row : sheet) {
                if (noEsPrimero) {
                    for(Cell cell : row){
                        switch(cell.getColumnIndex() + 1){
                            case 1:
                                celdaNombre = cell.getStringCellValue().trim();
                                celdaNombre = FunctionUtil.eliminaEspacios(celdaNombre);
                                break;
                            case 2:
                                celdaApellido = cell.getStringCellValue().trim();
                                celdaApellido = FunctionUtil.eliminaEspacios(celdaApellido);
                                break;
                            case 3:
                                if(cell.getCellType() == CellType.NUMERIC){
                                    celdaDni = String.valueOf((int)cell.getNumericCellValue()).trim();
                                }else{
                                    celdaDni = cell.getStringCellValue().trim();
                                    celdaDni = FunctionUtil.eliminaEspacios(celdaDni);
                                }
                                break;
                            case 4:
                                if(cell.getCellType() == CellType.NUMERIC){
                                    celdaContacto = String.valueOf((int)cell.getNumericCellValue()).trim();
                                }else{
                                    celdaContacto = cell.getStringCellValue().trim();
                                    celdaContacto = FunctionUtil.eliminaEspacios(celdaContacto);
                                }
                                break;
                            case 5:
                                celdaCorreo = cell.getStringCellValue().trim();
                                celdaCorreo = FunctionUtil.eliminaEspacios(celdaCorreo);
                                break;
                            case 6:
                                celdaDireccion = cell.getStringCellValue().trim();
                                celdaDireccion = FunctionUtil.eliminaEspacios(celdaDireccion);
                                break;
                            case 7:
                                celdaFechaNacimiento = cell.getStringCellValue().trim();
                                celdaFechaNacimiento = FunctionUtil.eliminaEspacios(celdaFechaNacimiento);
                                System.out.println("celdaFechaNacimiento: " + celdaFechaNacimiento);
                                break;
                            case 8:
                                celdaFechaRegistro = cell.getStringCellValue().trim();
                                celdaFechaRegistro = FunctionUtil.eliminaEspacios(celdaFechaRegistro);
                                System.out.println("celdaFechaRegistro: " + celdaFechaRegistro);
                                break;
//                            case 9:
//                                celdaAreaName = cell.getStringCellValue().trim();
//                                celdaAreaName = FunctionUtil.eliminaEspacios(celdaAreaName);
//                                break;
//                            case 10:
//                                celdaCargoName = cell.getStringCellValue().trim();
//                                celdaCargoName = FunctionUtil.eliminaEspacios(celdaCargoName);
//                                break;
                        }
                        Empleado objEmpleado = new Empleado();
                        objEmpleado.setNombre(celdaNombre);
                        objEmpleado.setApellido(celdaApellido);
                        objEmpleado.setDni(celdaDni);
                        objEmpleado.setContacto(celdaContacto);
                        objEmpleado.setCorreo(celdaCorreo);
                        objEmpleado.setDireccion(celdaDireccion);
                        objEmpleado.setFechaNacimiento(FunctionUtil.getFechaDate(celdaFechaNacimiento));
                        objEmpleado.setFechaRegistro(FunctionUtil.getFechaDate(celdaFechaRegistro));
//                        objEmpleado.setArea(areaRepository.findbyName(celdaAreaName));
//                        objEmpleado.setCargo(cargoRepository.findbyName(celdaCargoName));

                    }
                }
                noEsPrimero = true;
            }
            int ingresados = 0;
            for(Empleado objEmpleado : lstEmpleado){
                Empleado objSalida = empleadoRepository.save(objEmpleado);
                if(objSalida != null){
                    ingresados++;
                }
            }
            mensajes.put("mensaje", "Se ingresaron " + ingresados + " Empleados(s).");
            return mensajes;

        }catch(Exception e){
            e.printStackTrace();
            mensajes.put("error", "Error al leer el archivo");
        }finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
                if(workbook != null){
                    workbook.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return mensajes;
    }
    @Override
    public List<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }


//    private static String[] HEADERs ={"CÓDIGO","NOMBRES","APELLIDOS","DNI","CORREO","CONTACTO","FECHA DE NACIMIENTO","DIRECCION","AREA","CARGO"};
//    private static String SHEET = "Empleados";
//    private static String FILE_TITLE = "Listado de Empleados";
//    private static int[] HEADER_WIDTH = {3000, 8000,8000,6000,10000,6000,8000,6000,6000,6000};

//    @Override
//    public ByteArrayInputStream listarEmpleadosData(List<Empleado> lstEmpleado) {
//        return null;
//    }
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
