package com.prosesol.springboot.app.view.excel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IReportesExcel;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.Paises;

@Component
public class ReportesExcelImpl implements IReportesExcel {

	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
	private static final Log LOGGER = LogFactory.getLog(ReportesExcelImpl.class);

	private static String FILENAME = null;

	private static DataValidation dv = null;
	private static DataValidationConstraint dvConstraint = null;
	private static DataValidationHelper dvHelper = null;

	/**
	 * Método que genera el reporte del catálogo de afiliados en formato Xlsx
	 */
	
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
			encabezado.createCell(6).setCellValue("Fecha de Afiliación");
			encabezado.createCell(7).setCellValue("Fecha de Corte");
			encabezado.createCell(8).setCellValue("Periodo");
			encabezado.createCell(9).setCellValue("Email");

			int rowNum = 1;
			for (Afiliado afiliado : afiliados) {
				Row courseRow = sheet.createRow(rowNum++);

				sheet.autoSizeColumn(rowNum);

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
				courseRow.createCell(6).setCellValue(DATE_FORMAT.format(afiliado.getFechaAfiliacion()));
				courseRow.createCell(7).setCellValue(DATE_FORMAT.format(afiliado.getFechaCorte()));
				courseRow.createCell(8).setCellValue(afiliado.getPeriodicidad().getNombre());
				courseRow.createCell(9).setCellValue(afiliado.getEmail());
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

			makeRowBold(workbook, encabezado);

			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());

			workbook.close();

		} catch (Exception e) {
			LOGGER.error("Error al momento de generar el reporte", e);
		}

	}
	
	/**
	 * Método que genera el reporte del catálogo de afiliados en format Xls
	 */

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
			encabezado.createCell(6).setCellValue("Fecha de Afiliación");
			encabezado.createCell(7).setCellValue("Fecha de Corte");
			encabezado.createCell(8).setCellValue("Periodo");
			encabezado.createCell(9).setCellValue("Email");

			int rowNum = 1;
			for (Afiliado afiliado : afiliados) {
				Row courseRow = sheet.createRow(rowNum++);

				sheet.autoSizeColumn(rowNum);

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
				courseRow.createCell(6).setCellValue(DATE_FORMAT.format(afiliado.getFechaAfiliacion()));
				courseRow.createCell(7).setCellValue(DATE_FORMAT.format(afiliado.getFechaCorte()));
				courseRow.createCell(8).setCellValue(afiliado.getPeriodicidad().getNombre());
				courseRow.createCell(9).setCellValue(afiliado.getEmail());
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

			makeRowBold(workbook, encabezado);

			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());

			workbook.close();

		} catch (Exception e) {
			LOGGER.error("Error al momento de generar el reporte", e);
		}

	}
	
	/**
	 * Método que genera el template de carga masiva
	 */
	

	@Override
	public void generarTemplateAfiliadoXlsx(IAfiliadoService afiliadoService, IPeriodicidadService periodicidadService,
			IServicioService servicioService, IPromotorService promotorService, ICuentaService cuentaService,
			HttpServletResponse response) {

		FILENAME = "template.xlsx";

		Workbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Afiliado");
		Row encabezado = sheet.createRow(0);

		String[] afiliadoFields = afiliadoService.getVariablesAfiliado();

		try {

			int rowNum = 0;
			int cellRowNum = 0;
			for (int i = 3; i < afiliadoFields.length; i++) {
				sheet.autoSizeColumn(rowNum);

				encabezado.createCell(rowNum++).setCellValue(afiliadoFields[i]);

				switch (afiliadoFields[i]) {
				case "sexo":

					cellRowNum = rowNum - 1;

					String[] listSexo = { "Masculino", "Femenino" };

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addressListSexo = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(listSexo);
					dv = dvHelper.createValidation(dvConstraint, addressListSexo);

					dv.setSuppressDropDownArrow(true);
					
					dv.createPromptBox("Error", "Valor no permitido");
					dv.setErrorStyle(DataValidation.ErrorStyle.STOP);
					dv.createErrorBox("Error", "Valor no permitido");

					sheet.addValidationData(dv);

					break;

				case "pais":

					cellRowNum = rowNum - 1;
					List<Paises> listaPaises = afiliadoService.getAllPaises();

					String[] arrayPaises = new String[listaPaises.size()];

					int rowPaises = 0;
					for (Paises paises : listaPaises) {
						arrayPaises[rowPaises++] = paises.toString();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListPaises = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayPaises);
					dv = dvHelper.createValidation(dvConstraint, addresListPaises);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;

				case "entidadFederativa":

					cellRowNum = rowNum - 1;

					List<String> listaEstados = afiliadoService.getAllEstados();
					String[] arrayEstados = new String[listaEstados.size()];

					int rowEstados = 0;
					for (String estados : listaEstados) {
						arrayEstados[rowEstados++] = estados;
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListEstados = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayEstados);
					dv = dvHelper.createValidation(dvConstraint, addresListEstados);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;
				case "servicio":

					cellRowNum = rowNum - 1;

					List<Servicio> listaServicios = servicioService.findAll();
					String[] arrayServicios = new String[listaServicios.size()];

					int rowServicios = 0;
					for (Servicio servicios : listaServicios) {
						arrayServicios[rowServicios++] = servicios.getNombre();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListServicios = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayServicios);
					dv = dvHelper.createValidation(dvConstraint, addresListServicios);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;
				case "periodicidad":

					cellRowNum = rowNum - 1;
					List<Periodicidad> listaPeriodos = periodicidadService.findAll();
					String[] arrayPeriodos = new String[listaPeriodos.size()];

					int rowPeriodos = 0;
					for (Periodicidad periodos : listaPeriodos) {
						arrayPeriodos[rowPeriodos++] = periodos.getNombre();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListPeriodos = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayPeriodos);
					dv = dvHelper.createValidation(dvConstraint, addresListPeriodos);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;
				case "promotor":

					cellRowNum = rowNum - 1;

					List<Promotor> listaPromotores = promotorService.findAll();
					String[] arrayPromotor = new String[listaPromotores.size()];

					int rowPromotor = 0;
					for (Promotor promotor : listaPromotores) {
						arrayPromotor[rowPromotor++] = promotor.getNombre();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListPromotor = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayPromotor);
					dv = dvHelper.createValidation(dvConstraint, addresListPromotor);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;

				case "cuenta":

					cellRowNum = rowNum - 1;
					List<Cuenta> listaCuentas = cuentaService.findAll();
					String[] arrayCuentas = new String[listaCuentas.size()];

					int rowCuentas = 0;
					for (Cuenta cuentas : listaCuentas) {
						arrayCuentas[rowCuentas++] = cuentas.getRazonSocial();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListCuenta = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayCuentas);
					dv = dvHelper.createValidation(dvConstraint, addresListCuenta);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;

				case "corte":

					cellRowNum = rowNum - 1;
					List<Integer> listaCorte = new ArrayList<Integer>();

					for (int j = 1; j <= 31; j++) {
						listaCorte.add(j);
					}

					String[] arrayCorte = new String[listaCorte.size()];

					int rowCorte = 0;
					for (Integer corte : listaCorte) {
						arrayCorte[rowCorte++] = corte.toString();
					}

					dvHelper = new XSSFDataValidationHelper(sheet);
					CellRangeAddressList addresListCorte = new CellRangeAddressList(1, 1, cellRowNum, cellRowNum);
					dvConstraint = dvHelper.createExplicitListConstraint(arrayCorte);
					dv = dvHelper.createValidation(dvConstraint, addresListCorte);

					dv.setSuppressDropDownArrow(true);

					sheet.addValidationData(dv);

					break;

				}
			}

			makeRowBold(workbook, encabezado);

			response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
			workbook.write(response.getOutputStream());

			workbook.close();

		} catch (Exception e) {
			LOGGER.error("Error al momento de generar el template", e);
		}

	}

	/**
	 * Método que lee el archivo de carga masiva
	 */
	
	public void leerArchivoCargaMasiva(XSSFWorkbook workbook) {
		
		long startTime = System.nanoTime();
		
		Sheet sheet = workbook.getSheetAt(0);
		
		Row row;
		Cell cell;
		
		@SuppressWarnings("unused")
		int numOfRow = 0;
		@SuppressWarnings("unused")
		int numOfCol = 0;
		
		@SuppressWarnings("rawtypes")
		Iterator rows = sheet.rowIterator();
		
		@SuppressWarnings("resource")
		SXSSFWorkbook SXSSF_wb = new SXSSFWorkbook(1000);
		
		while(rows.hasNext()) {
			
			numOfRow++;
			
			row = (Row)rows.next();
			@SuppressWarnings("rawtypes")
			Iterator cells = row.cellIterator();
			
			while(cells.hasNext()) {
				
				numOfCol++;
				
				cell = (Cell)cells.next();
				
				if(cell.getCellType() == CellType.STRING) {
					
					System.out.println("Celdas tipo String: " + cell.getStringCellValue());
					
				}else if(cell.getCellType() == CellType.NUMERIC) {
					
					System.out.println("Celdas tipo numéricas: " + cell.getNumericCellValue());
					
				}				
			}
			
			numOfCol = 0;			
		}
		
		System.gc();
		SXSSF_wb.dispose();
		
		long endTime = System.nanoTime();
		
		LOGGER.info("Tiempo de ejecución en segundos " + (endTime-startTime)/1000000000);
		
	}
	
	private static void makeRowBold(Workbook workbook, Row row) {

		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);

		for (int i = 0; i < row.getLastCellNum(); i++) {
			row.getCell(i).setCellStyle(style);
		}

	}

}
