package com.prosesol.springboot.app.view.excel;

import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.exception.CustomValidatorExcelException;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IPagoService;
import com.prosesol.springboot.app.service.IReportesExcel;
import com.prosesol.springboot.app.service.IServicioService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

@Component
public class ReportesExcelImpl implements IReportesExcel {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    private static final Log LOGGER = LogFactory.getLog(ReportesExcelImpl.class);

    private static String FILENAME = null;

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ValidatorExcel validatorExcel;
    
    @Autowired
    private IPagoService pagoService;

    /**
     * Método que genera el template de carga masiva
     */

    @Override
    public void generarTemplateAfiliadoXlsx(HttpServletResponse response) throws CustomValidatorExcelException{

        FILENAME = "template.xlsx";

        Workbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Afiliado");
        XSSFSheet sheetServicios = (XSSFSheet)workbook.createSheet("Servicios");
        Row header = sheet.createRow(0);
        Row headerServicio = sheetServicios.createRow(0);

        String[] afiliadoFields = afiliadoService.getVariablesAfiliado();
        String[] encabezado = validatorExcel.generarEncabezado(afiliadoFields, sheet);

        try {

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short)14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(headerFont);

            for(int i = 0; i < encabezado.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(encabezado[i]);
                cell.setCellStyle(cellStyle);
            }

            Cell cellHeaderServicio = headerServicio.createCell(0);
            cellHeaderServicio.setCellValue("ID");
            cellHeaderServicio.setCellStyle(cellStyle);

            Cell cellServicio = headerServicio.createCell(1);
            cellServicio.setCellValue("Servicios");
            cellServicio.setCellStyle(cellStyle);

            for(int i = 0; i < encabezado.length; i++){
                sheet.autoSizeColumn(i);
            }

            List<Servicio> listServicios = servicioService.findAll();

            int rowNum = 1;
            for(Servicio servicio : listServicios){
                Row row = sheetServicios.createRow(rowNum++);

                row.createCell(0).setCellValue(servicio.getId());
                row.createCell(1).setCellValue(servicio.getNombre());
            }

            sheetServicios.autoSizeColumn(0);

            response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
            workbook.write(response.getOutputStream());

            workbook.close();

        } catch (CustomValidatorExcelException e) {
            e.printStackTrace();
        }catch(IOException ie){
            throw new CustomValidatorExcelException(ie.getMessage());
        }

    }
    
    /**
     * Método que genera el template conciliación de pagos de afiliados
     */

    @Override
    public void generarTemplateConciliacionPagosAfiliadoXlsx(HttpServletResponse response) throws CustomValidatorExcelException{
    	 FILENAME = "template.xlsx";
    	 
    	 Workbook workbook = new XSSFWorkbook();
         XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Pagos Afiliados");
         //XSSFSheet sheetServicios = (XSSFSheet)workbook.createSheet("Servicios");
         Row header = sheet.createRow(0);
         //Row headerServicio = sheetServicios.createRow(0);

         String[] pagosFields = pagoService.getVariablesPagos();
         String[] encabezado = validatorExcel.generarEncabezadoPagos(pagosFields, sheet);

         try {

             Font headerFont = workbook.createFont();
             headerFont.setBold(true);
             headerFont.setFontHeightInPoints((short)14);
             headerFont.setColor(IndexedColors.BLACK.getIndex());

             CellStyle cellStyle = workbook.createCellStyle();
             cellStyle.setFont(headerFont);

             for(int i = 0; i < encabezado.length; i++) {
                 Cell cell = header.createCell(i);
                 cell.setCellValue(encabezado[i]);
                 cell.setCellStyle(cellStyle);
             }

         /*    Cell cellHeaderServicio = headerServicio.createCell(0);
             cellHeaderServicio.setCellValue("ID");
             cellHeaderServicio.setCellStyle(cellStyle);

             Cell cellServicio = headerServicio.createCell(1);
             cellServicio.setCellValue("Servicios");
             cellServicio.setCellStyle(cellStyle);

             for(int i = 0; i < encabezado.length; i++){
                 sheet.autoSizeColumn(i);
             }

             List<Servicio> listServicios = servicioService.findAll();

             int rowNum = 1;
             for(Servicio servicio : listServicios){
                 Row row = sheetServicios.createRow(rowNum++);

                 row.createCell(0).setCellValue(servicio.getId());
                 row.createCell(1).setCellValue(servicio.getNombre());
             }

             sheetServicios.autoSizeColumn(0);*/

             response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
             workbook.write(response.getOutputStream());

             workbook.close();

         } catch (CustomValidatorExcelException e) {
             e.printStackTrace();
         }catch(IOException ie){
             throw new CustomValidatorExcelException(ie.getMessage());
         }
    }
    


}
