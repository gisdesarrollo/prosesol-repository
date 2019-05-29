package com.prosesol.springboot.app.view.excel;

import java.text.DateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IReportesExcel;

@Component
public class ReportesExcelImpl implements IReportesExcel {

	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
	private static final Log LOGGER = LogFactory.getLog(ReportesExcelImpl.class);

	private static String FILENAME = null;

	@Override
	public void generarReporteAfiliadoXlsx(List<Afiliado> afiliados, HttpServletResponse response) {

		FILENAME = "reporte_afiliados.xlsx";

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Reporte Afiliados");
		Row encabezado = sheet.createRow(0);

		try {

			encabezado.createCell(0).setCellValue("Nombre Afiliado");
			encabezado.createCell(1).setCellValue("Tipo de Afiliado");
			encabezado.createCell(2).setCellValue("Estatus");
			encabezado.createCell(3).setCellValue("Servicio");
			encabezado.createCell(4).setCellValue("Saldo Acumulado");
			encabezado.createCell(5).setCellValue("Saldo al Corte");
			encabezado.createCell(6).setCellValue("Fecha de Inicio de Servicio");
			encabezado.createCell(7).setCellValue("Fecha de Corte");
			encabezado.createCell(8).setCellValue("Fecha de Afiliación");
			encabezado.createCell(9).setCellValue("Periodo");
			encabezado.createCell(10).setCellValue("Email");

			int rowNum = 1;
			for (Afiliado afiliado : afiliados) {
				Row courseRow = sheet.createRow(rowNum++);
				courseRow.createCell(0).setCellValue(afiliado.getNombre() + " " + afiliado.getApellidoPaterno() + " "
						+ afiliado.getApellidoMaterno());
				courseRow.createCell(1).setCellValue(afiliado.getIsBeneficiario() ? "Beneficiario" : "Titular");

				if (afiliado.getEstatus() == 1) {
					courseRow.createCell(2).setCellValue("Activo");
				} else if (afiliado.getEstatus() == 2) {
					courseRow.createCell(2).setCellValue("Inactivo");
				} else if (afiliado.getEstatus() == 3) {
					courseRow.createCell(2).setCellValue("Candidato");
				}

				courseRow.createCell(3).setCellValue(afiliado.getServicio().getNombre());
				courseRow.createCell(4).setCellValue(afiliado.getSaldoAcumulado());
				courseRow.createCell(5).setCellValue(afiliado.getSaldoCorte());
				courseRow.createCell(6).setCellValue(DATE_FORMAT.format(afiliado.getFechaInicioServicio()));
				courseRow.createCell(7).setCellValue(DATE_FORMAT.format(afiliado.getFechaCorte()));
				courseRow.createCell(8).setCellValue(DATE_FORMAT.format(afiliado.getFechaAfiliacion()));
				courseRow.createCell(9).setCellValue(afiliado.getPeriodicidad().getNombre());
				courseRow.createCell(10).setCellValue(afiliado.getEmail());
			}

			CellStyle theaderStyle = workbook.createCellStyle();
			theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
			theaderStyle.setBorderTop(BorderStyle.MEDIUM);
			theaderStyle.setBorderRight(BorderStyle.MEDIUM);
			theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
			theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
			theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			CellStyle tbodyStyle = workbook.createCellStyle();
			tbodyStyle.setBorderBottom(BorderStyle.THIN);
			tbodyStyle.setBorderTop(BorderStyle.THIN);
			tbodyStyle.setBorderRight(BorderStyle.THIN);
			tbodyStyle.setBorderLeft(BorderStyle.THIN);

			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());

			workbook.close();

		} catch (Exception e) {
			LOGGER.error("Error al momento de generar el reporte", e);
		}

	}

	@Override
	public void generarReporteAfiliadoXls(List<Afiliado> afiliados, HttpServletResponse response) {

		FILENAME = "reporte_afiliados.xls";

		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Reporte Afiliados");
		Row encabezado = sheet.createRow(0);

		try {

			encabezado.createCell(0).setCellValue("Nombre Afiliado");
			encabezado.createCell(1).setCellValue("Tipo de Afiliado");
			encabezado.createCell(2).setCellValue("Estatus");
			encabezado.createCell(3).setCellValue("Servicio");
			encabezado.createCell(4).setCellValue("Saldo Acumulado");
			encabezado.createCell(5).setCellValue("Saldo al Corte");
			encabezado.createCell(6).setCellValue("Fecha de Inicio de Servicio");
			encabezado.createCell(7).setCellValue("Fecha de Corte");
			encabezado.createCell(8).setCellValue("Fecha de Afiliación");
			encabezado.createCell(9).setCellValue("Periodo");
			encabezado.createCell(10).setCellValue("Email");

			int rowNum = 1;
			for (Afiliado afiliado : afiliados) {
				Row courseRow = sheet.createRow(rowNum++);
				courseRow.createCell(0).setCellValue(afiliado.getNombre() + " " + afiliado.getApellidoPaterno() + " "
						+ afiliado.getApellidoMaterno());
				courseRow.createCell(1).setCellValue(afiliado.getIsBeneficiario() ? "Beneficiario" : "Titular");

				if (afiliado.getEstatus() == 1) {
					courseRow.createCell(2).setCellValue("Activo");
				} else if (afiliado.getEstatus() == 2) {
					courseRow.createCell(2).setCellValue("Inactivo");
				} else if (afiliado.getEstatus() == 3) {
					courseRow.createCell(2).setCellValue("Candidato");
				}

				courseRow.createCell(3).setCellValue(afiliado.getServicio().getNombre());
				courseRow.createCell(4).setCellValue(afiliado.getSaldoAcumulado());
				courseRow.createCell(5).setCellValue(afiliado.getSaldoCorte());
				courseRow.createCell(6).setCellValue(DATE_FORMAT.format(afiliado.getFechaInicioServicio()));
				courseRow.createCell(7).setCellValue(DATE_FORMAT.format(afiliado.getFechaCorte()));
				courseRow.createCell(8).setCellValue(DATE_FORMAT.format(afiliado.getFechaAfiliacion()));
				courseRow.createCell(9).setCellValue(afiliado.getPeriodicidad().getNombre());
				courseRow.createCell(10).setCellValue(afiliado.getEmail());
			}

			CellStyle theaderStyle = workbook.createCellStyle();
			theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
			theaderStyle.setBorderTop(BorderStyle.MEDIUM);
			theaderStyle.setBorderRight(BorderStyle.MEDIUM);
			theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
			theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
			theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			CellStyle tbodyStyle = workbook.createCellStyle();
			tbodyStyle.setBorderBottom(BorderStyle.THIN);
			tbodyStyle.setBorderTop(BorderStyle.THIN);
			tbodyStyle.setBorderRight(BorderStyle.THIN);
			tbodyStyle.setBorderLeft(BorderStyle.THIN);

			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());

			workbook.close();

		} catch (Exception e) {
			LOGGER.error("Error al momento de generar el reporte", e);
		}

	}

	@Override
	public void generarTemplateAfiliadoXlsx(IAfiliadoService afiliadoService, HttpServletResponse response) {

		FILENAME = "template.xlsx";
		
		Workbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Afiliado");
		Row encabezado = sheet.createRow(0);
		
		String[] afiliadoFields = afiliadoService.getVariablesAfiliado();
		
		try {
						
			int rowNum = 0;
			for(int i = 3; i < afiliadoFields.length; i++) {			
				sheet.autoSizeColumn(i);
				encabezado.createCell(rowNum++).setCellValue(afiliadoFields[i]);				
			}
			
			
			makeRowBold(workbook, encabezado);
			
			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());
			
			workbook.close();
			
		}catch(Exception e) {
			LOGGER.error("Erro al momento de generar el template", e);
		}
		
	}
	
	private static void makeRowBold(Workbook workbook, Row row) {
		
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		
		for(int i = 0; i < row.getLastCellNum(); i++) {
			row.getCell(i).setCellStyle(style);
		}
		
	}

}
