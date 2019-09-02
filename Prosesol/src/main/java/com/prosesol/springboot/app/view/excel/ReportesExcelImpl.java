package com.prosesol.springboot.app.view.excel;

import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.util.Paises;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportesExcelImpl implements IReportesExcel {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    private static final Log LOGGER = LogFactory.getLog(ReportesExcelImpl.class);

    private static String FILENAME = null;

    private static DataValidation dv = null;
    private static DataValidationConstraint dvConstraint = null;
    private static DataValidationHelper dvHelper = null;

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private IPeriodicidadService periodicidadService;

    @Autowired
    private ICuentaService cuentaService;

    @Autowired
    private IPromotorService promotorService;

    @Autowired
    private IServicioService servicioService;

    /**
     * Método que genera el template de carga masiva
     */

    @Override
    public void generarTemplateAfiliadoXlsx(HttpServletResponse response) {

        FILENAME = "template.csv";

        Workbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Afiliado");
        Row encabezado = sheet.createRow(0);

        String[] afiliadoFields = afiliadoService.getVariablesAfiliado();

        try {

            int rowNum = 0;
            int cellRowNum = 0;
            for (int i = 3; i < afiliadoFields.length; i++) {
                sheet.autoSizeColumn(rowNum);

                if (afiliadoFields[i].equals("fechaAlta") || afiliadoFields[i].equals("saldoAcumulado")
                        || afiliadoFields[i].equals("saldoCorte") || afiliadoFields[i].equals("estatus")
                        || afiliadoFields[i].equals("inscripcion") || afiliadoFields[i].equals("fechaCorte")
                        || afiliadoFields[i].equals("relAfiliadoIncidencia") || afiliadoFields[i].equals("infonavit")) {
                    continue;
                } else if (afiliadoFields[i].equals("isBeneficiario")) {
                    encabezado.createCell(rowNum++).setCellValue("¿Es Beneficiario? (Sí/No)");
                } else if (afiliadoFields[i].equals("beneficiarios")) {
                    encabezado.createCell(rowNum++).setCellValue("RFC Afiliado (Si es beneficiario)");
                } else {
                    encabezado.createCell(rowNum++).setCellValue(afiliadoFields[i]);
                }

                switch (afiliadoFields[i]) {

                    case "estadoCivil":

                        CellRangeAddressList addressListEstadoCivil = null;

                        cellRowNum = rowNum - 1;

                        String[] listEstadoCivil = {"Soltero(a)", "Casado(a)", "Viudo(a)", "Divorciado(a)",
                                "Desconocido"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListEstadoCivil = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listEstadoCivil);
                        dv = dvHelper.createValidation(dvConstraint, addressListEstadoCivil);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "sexo":

                        CellRangeAddressList addressListSexo = null;

                        cellRowNum = rowNum - 1;

                        String[] listSexo = {"Masculino", "Femenino"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListSexo = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listSexo);
                        dv = dvHelper.createValidation(dvConstraint, addressListSexo);

                        dv.setSuppressDropDownArrow(true);

                        dv.createPromptBox("Error", "Valor no permitido");
                        dv.setErrorStyle(DataValidation.ErrorStyle.STOP);
                        dv.createErrorBox("Error", "Valor no permitido");

                        sheet.addValidationData(dv);

                        break;

                    case "pais":

                        CellRangeAddressList addresListPaises = null;

                        cellRowNum = rowNum - 1;
                        List<Paises> listaPaises = afiliadoService.getAllPaises();

                        String[] arrayPaises = new String[listaPaises.size()];

                        int rowPaises = 0;
                        for (Paises paises : listaPaises) {
                            arrayPaises[rowPaises++] = paises.toString();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListPaises = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayPaises);
                        dv = dvHelper.createValidation(dvConstraint, addresListPaises);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "entidadFederativa":

                        CellRangeAddressList addresListEstados = null;

                        cellRowNum = rowNum - 1;

                        List<String> listaEstados = afiliadoService.getAllEstados();
                        String[] arrayEstados = new String[listaEstados.size()];

                        int rowEstados = 0;
                        for (String estados : listaEstados) {
                            arrayEstados[rowEstados++] = estados;
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListEstados = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayEstados);
                        dv = dvHelper.createValidation(dvConstraint, addresListEstados);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "infonavit":

                        CellRangeAddressList addresListInfonavit = null;

                        cellRowNum = rowNum - 1;

                        String[] arrayInfonavit = {"Sí", "No"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListInfonavit = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayInfonavit);
                        dv = dvHelper.createValidation(dvConstraint, addresListInfonavit);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "servicio":

                        CellRangeAddressList addresListServicios = null;

                        cellRowNum = rowNum - 1;

                        List<Servicio> listaServicios = servicioService.findAll();
                        String[] arrayServicios = new String[listaServicios.size()];

                        int rowServicios = 0;
                        for (Servicio servicios : listaServicios) {
                            arrayServicios[rowServicios++] = servicios.getNombre();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListServicios = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayServicios);
                        dv = dvHelper.createValidation(dvConstraint, addresListServicios);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;
                    case "periodicidad":

                        CellRangeAddressList addresListPeriodos = null;

                        cellRowNum = rowNum - 1;
                        List<Periodicidad> listaPeriodos = periodicidadService.findAll();
                        String[] arrayPeriodos = new String[listaPeriodos.size()];

                        int rowPeriodos = 0;
                        for (Periodicidad periodos : listaPeriodos) {
                            arrayPeriodos[rowPeriodos++] = periodos.getNombre();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListPeriodos = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayPeriodos);
                        dv = dvHelper.createValidation(dvConstraint, addresListPeriodos);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "isBeneficiario":

                        CellRangeAddressList addresListIsBeneficiario = null;

                        cellRowNum = rowNum - 1;

                        String[] arrayIsBeneficiario = {"Sí", "No"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListIsBeneficiario = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayIsBeneficiario);
                        dv = dvHelper.createValidation(dvConstraint, addresListIsBeneficiario);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;
                    case "promotor":

                        CellRangeAddressList addresListPromotor = null;

                        cellRowNum = rowNum - 1;

                        List<Promotor> listaPromotores = promotorService.findAll();
                        String[] arrayPromotor = new String[listaPromotores.size()];

                        int rowPromotor = 0;
                        for (Promotor promotor : listaPromotores) {
                            arrayPromotor[rowPromotor++] = promotor.getNombre();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListPromotor = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayPromotor);
                        dv = dvHelper.createValidation(dvConstraint, addresListPromotor);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "cuenta":

                        CellRangeAddressList addresListCuenta = null;

                        cellRowNum = rowNum - 1;
                        List<Cuenta> listaCuentas = cuentaService.findAll();
                        String[] arrayCuentas = new String[listaCuentas.size()];

                        int rowCuentas = 0;
                        for (Cuenta cuentas : listaCuentas) {
                            arrayCuentas[rowCuentas++] = cuentas.getRazonSocial();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addresListCuenta = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayCuentas);
                        dv = dvHelper.createValidation(dvConstraint, addresListCuenta);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                    case "corte":

                        CellRangeAddressList addresListCorte = null;

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

                        for (int x = 1; x < 999; x++) {
                            addresListCorte = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayCorte);
                        dv = dvHelper.createValidation(dvConstraint, addresListCorte);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                }

                sheet.autoSizeColumn(i);

            }

            makeRowBold(workbook, encabezado);

            response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
            workbook.write(response.getOutputStream());

            workbook.close();

        } catch (Exception e) {
            LOGGER.error("Error al momento de generar el template", e);
        }

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
