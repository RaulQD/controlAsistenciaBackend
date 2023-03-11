package com.proyecto.controlasistenciabackend.service;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.repository.AreaRepository;
import com.proyecto.controlasistenciabackend.repository.CargoRepository;
import com.proyecto.controlasistenciabackend.repository.UsuarioRepository;
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

import javax.naming.directory.AttributeInUseException;
import javax.print.AttributeException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    public void eliminarEmpleado(int idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    @Override
    public Usuario buscarEmpleadoPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null);
    }

    private static String[] HEADERS = {"CODIGO", "NOMBRE", "APELLIDO", "DNI", "CORREO","CONTACTO","DIRECCION","FECHA_NACIMIENTO","FECHA_REGISTRO","TARIFA","AREA", "CARGO","ESTADO"};
    private static String SHEET = "LISTADO";
    private static String TITLE = "LISTADO DE EMPLEADOS";
    private static int[] HEADER_WIDTH = {3000,6000,6000,6000,8000,6000,6000,3000,3000,6000,7000,5000,5000,4000};
    @Override
    public ByteArrayInputStream exportarUsuarioExcel(List<Usuario> lstUsuarios) {
        try(Workbook excel = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {

            //TODO:CREAR EL EXCEL
            Sheet hoja = excel.createSheet(SHEET);
            hoja.addMergedRegion(new CellRangeAddress(0,0,0,HEADER_WIDTH.length - 1));

            for(int i = 0; i < HEADER_WIDTH.length; i++){
                hoja.setColumnWidth(i,HEADER_WIDTH[i]);
            }
            CellStyle estiloHeaderCentrado = UtilExcel.setEstiloHeadCentrado(excel);
            CellStyle estiloHeaderIzquierda = UtilExcel.setEstiloHeadIzquierda(excel);
            CellStyle estiloNormalCentrado = UtilExcel.setEstiloNormalCentrado(excel);
            CellStyle estiloNormalIzquierda = UtilExcel.setEstiloNormalIzquierdo(excel);

            Row filaTitulo = hoja.createRow(0);
            Cell celTitulo = filaTitulo.createCell(0);
            celTitulo.setCellStyle(estiloHeaderCentrado);
            celTitulo.setCellValue(TITLE);

            Row fila2 = hoja.createRow(1);
            Cell cel2 = fila2.createCell(0);
            cel2.setCellValue("");

            Row fila3 = hoja.createRow(2);
            for(int i = 0; i < HEADERS.length; i++){
                Cell cel3 = fila3.createCell(i);
                cel3.setCellStyle(estiloHeaderCentrado);
                cel3.setCellValue(HEADERS[i]);
            }
            int rowIdx = 3, rowIndex = 0;
            for(Usuario obj: lstUsuarios){
                Row row = hoja.createRow(rowIdx++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(obj.getIdUsuario());
                cell0.setCellStyle(estiloNormalCentrado);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(obj.getNombre());
                cell1.setCellStyle(estiloNormalIzquierda);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(obj.getApellido());
                cell2.setCellStyle(estiloNormalIzquierda);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(obj.getDni());
                cell3.setCellStyle(estiloNormalCentrado);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(obj.getCorreo());
                cell4.setCellStyle(estiloNormalIzquierda);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(obj.getContacto());
                cell5.setCellStyle(estiloNormalCentrado);

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(obj.getDireccion());
                cell6.setCellStyle(estiloNormalIzquierda);

                Cell cell7 = row.createCell(7);
                cell7.setCellValue(FunctionUtil.getFechaString(obj.getFechaNacimiento()));
                cell7.setCellStyle(estiloNormalCentrado);

                Cell cell8 = row.createCell(8);
                cell8.setCellValue(FunctionUtil.getFechaString(obj.getFechaRegistro()));
                cell8.setCellStyle(estiloNormalCentrado);

                Cell cell9 = row.createCell(9);
                cell9.setCellValue(obj.getTarifa());
                cell9.setCellStyle(estiloNormalCentrado);

                Cell cell10 = row.createCell(10);
                cell10.setCellValue(obj.getArea().getArea());
                cell10.setCellStyle(estiloNormalIzquierda);

                Cell cell11 = row.createCell(11);
                cell11.setCellValue(obj.getCargo().getCargo());
                cell11.setCellStyle(estiloNormalIzquierda);

                Cell cell12 = row.createCell(12);
                cell12.setCellValue(obj.getEstado());
                cell12.setCellStyle(estiloNormalIzquierda);

            }
            excel.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al exportar el excel: " + e.getMessage());
        }
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
