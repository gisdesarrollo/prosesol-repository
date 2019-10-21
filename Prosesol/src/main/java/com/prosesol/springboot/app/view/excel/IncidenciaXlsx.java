package com.prosesol.springboot.app.view.excel;

import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.service.IServicioService;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IncidenciaXlsx extends AbstractXlsxView {

    @Autowired
    private IServicioService servicioService;

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"Reporte_" +
                new Date() +".xlsx\"");

        List<Incidencia> incidencias = (List<Incidencia>)model.get("incidencias");
        Sheet sheet = workbook.createSheet("Reporte de Incidencias");

        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        cellStyle.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nombre del Afiliado");
        sheet.autoSizeColumn(0);
        header.createCell(1).setCellValue("Fecha de Incidencia");
        sheet.autoSizeColumn(1);
        header.createCell(2).setCellValue("Hora de Incidencia");
        sheet.autoSizeColumn(2);
        header.createCell(3).setCellValue("Localización");
        sheet.autoSizeColumn(3);
        header.createCell(4).setCellValue("Tipo de incidencia");
        sheet.autoSizeColumn(4);
        header.createCell(5).setCellValue("Detalle");
        sheet.autoSizeColumn(5);
        header.createCell(6).setCellValue("Proveedor");
        sheet.autoSizeColumn(6);
        header.createCell(7).setCellValue("Estatus");
        sheet.autoSizeColumn(7);
        header.createCell(8).setCellValue("Fecha de Creación de Incidencia");
        sheet.autoSizeColumn(8);
        header.createCell(9).setCellValue("Servicio");
        sheet.autoSizeColumn(9);

        int rowNum = 1;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for(Incidencia incidencia : incidencias){
            Row row = sheet.createRow(rowNum++);

            String nombreServicio = servicioService
                    .getNombreByIdIncidencia(incidencia.getId());

            row.createCell(0).setCellValue(incidencia.getNombreAfiliado());
            String fecha = dateFormat.format(incidencia.getFecha());
            row.createCell(1).setCellValue(fecha);
            row.createCell(2).setCellValue(incidencia.getHora());
            row.createCell(3).setCellValue(incidencia.getLocalizacion());
            row.createCell(4).setCellValue(incidencia.getTipoIncidencia());

            // Convertir descripción del detalle a UTF-8

            String detalle = StringEscapeUtils.unescapeHtml4(incidencia.getDetalle());

            row.createCell(5).setCellValue(detalle);
            row.createCell(6).setCellValue(incidencia.getProveedor());
            switch (incidencia.getEstatus()){
                case 1:
                    row.createCell(7).setCellValue("Abierto");
                    break;
                case 2:
                    row.createCell(7).setCellValue("En proceso");
                    break;
                case 3:
                    row.createCell(7).setCellValue("Completado");
                    break;
                case 4:
                    row.createCell(7).setCellValue("Cancelado");
                    break;
            }

            String fechaCreacion = dateFormat.format(incidencia.getFechaCreacion());
            row.createCell(8).setCellValue(fechaCreacion);
            row.createCell(9).setCellValue(nombreServicio);

            sheet.autoSizeColumn(rowNum);

        }
    }
}
