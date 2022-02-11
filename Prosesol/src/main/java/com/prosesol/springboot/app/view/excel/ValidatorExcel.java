package com.prosesol.springboot.app.view.excel;

import com.prosesol.springboot.app.exception.CustomValidatorExcelException;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.util.Paises;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidatorExcel {

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private IPeriodicidadService periodicidadService;

    @Autowired
    private ICuentaService cuentaService;

    @Autowired
    private IPromotorService promotorService;

    private static DataValidation dv = null;
    private static DataValidationConstraint dvConstraint = null;
    private static DataValidationHelper dvHelper = null;

    public String[] generarEncabezado(String[] afiliadoFields, XSSFSheet sheet)throws CustomValidatorExcelException{

        String[] encabezado = new String[afiliadoFields.length];

        int rowNum = 0;
        int cellRowNum = 0;

        try {

            for (int i = 0; i < afiliadoFields.length; i++) {
                switch (afiliadoFields[i]) {
                    case "nombre":
                        encabezado[rowNum++] = "Nombre";
                        break;
                    case "apellidoPaterno":
                        encabezado[rowNum++] = "Apellido Paterno";
                        break;
                    case "apellidoMaterno":
                        encabezado[rowNum++] = "Apellido Materno";
                        break;
                    case "fechaNacimiento":
                        encabezado[rowNum++] = "Fecha de Nacimiento";
                        break;
                    case "lugarNacimiento":
                        encabezado[rowNum++] = "Lugar de Nacimiento";
                        break;
                    case "estadoCivil":
                        encabezado[rowNum++] = "Estado Civil";

                        CellRangeAddressList addressListEstadoCivil = null;
                        cellRowNum = rowNum - 1;

                        String [] listEstadoCivil = {"Soltero(a), Casado(a), Viudo(a), Divorciado(a), Desconocido"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for(int x  = 1; x < 999; x++){
                            addressListEstadoCivil = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listEstadoCivil);
                        dv = dvHelper.createValidation(dvConstraint, addressListEstadoCivil);

                        dv.setSuppressDropDownArrow(true);
                        sheet.addValidationData(dv);

                        break;
                    case "numeroDependientes":
                        encabezado[rowNum++] = "Número de Dependientes";
                        break;
                    case "ocupacion":
                        encabezado[rowNum++] = "Ocupación";
                        break;
                    case "sexo":
                        encabezado[rowNum++] = "Sexo";

                        CellRangeAddressList addressListSexo = null;
                        cellRowNum = rowNum - 1;

                        String[] listSexo = generateListSexo();

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListSexo = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listSexo);
                        dv = dvHelper.createValidation(dvConstraint, addressListSexo);

                        dv.setSuppressDropDownArrow(true);

                        dv.createPromptBox("Error", "Valod no permitido");
                        dv.setErrorStyle(DataValidation.ErrorStyle.STOP);
                        dv.createErrorBox("Error", "Valor no permitido");

                        sheet.addValidationData(dv);

                        break;
                    case "pais":
                        encabezado[rowNum++] = "País";

                        CellRangeAddressList addressListPais = null;

                        cellRowNum = rowNum - 1;

                        List<Paises> listaPaises = afiliadoService.getAllPaises();
                        String[] arrayPaises = new String[listaPaises.size()];

                        int rowPaises = 0;
                        for (Paises paises : listaPaises) {
                            arrayPaises[rowPaises++] = paises.toString();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListPais = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayPaises);
                        dv = dvHelper.createValidation(dvConstraint, addressListPais);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);
                        break;
                    case "curp":
                        encabezado[rowNum++] = "CURP";
                        break;
                    case "nss":
                        encabezado[rowNum++] = "NSS";
                        break;
                    case "rfc":
                        encabezado[rowNum++] = "RFC";
                        break;
                    case "telefonoFijo":
                        encabezado[rowNum++] = "Teléfono Fijo";
                        break;
                    case "telefonoMovil":
                        encabezado[rowNum++] = "Teléfono Móvil";
                        break;
                    case "email":
                        encabezado[rowNum++] = "Correo electrónico";
                        break;
                    case "direccion":
                        encabezado[rowNum++] = "Dirección";
                        break;
                    case "municipio":
                        encabezado[rowNum++] = "Municipio";
                        break;
                    case "codigoPostal":
                        encabezado[rowNum++] = "Código Postal";
                        break;
                    case "entidadFederativa":
                        encabezado[rowNum++] = "Entidad Federativa";

                        CellRangeAddressList addressListEstados = null;

                        cellRowNum = rowNum - 1;

                        List<String> listaEstados = afiliadoService.getAllEstados();
                        String[] arrayEstados = new String[listaEstados.size()];

                        int rowEstados = 0;
                        for (String estados : listaEstados) {
                            arrayEstados[rowEstados++] = estados;
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListEstados = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayEstados);
                        dv = dvHelper.createValidation(dvConstraint, addressListEstados);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;
                    case "numeroInfonavit":
                        encabezado[rowNum++] = "Número de infonavit";
                        break;
                    case "fechaAfiliacion":
                        encabezado[rowNum++] = "Fecha de afiliación";
                        break;
                    case "servicio":
                        encabezado[rowNum++] = "Servicio";
                        break;
                    case "periodicidad":
                        encabezado[rowNum++] = "Periodo";
                        break;
                    case "comentarios":
                        encabezado[rowNum++] = "Comentarios";
                        break;
                    case "isBeneficiario":
                        encabezado[rowNum++] = "¿Es Beneficiario?";

                        CellRangeAddressList addressListIsBeneficiario = null;

                        cellRowNum = rowNum - 1;

                        String[] arrayIsBeneficiario = {"Sí", "No"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListIsBeneficiario = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayIsBeneficiario);
                        dv = dvHelper.createValidation(dvConstraint, addressListIsBeneficiario);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;
                    case "beneficiarios":
                        encabezado[rowNum++] = "RFC Afiliado(Si es beneficiario)";
                        break;
                    case "promotor":
                        encabezado[rowNum++] = "Promotor";
                        break;
                    case "cuenta":
                        encabezado[rowNum++] = "Cuenta Comercial";
                        break;
                    case "corte":
                        encabezado[rowNum++] = "Corte";

                        CellRangeAddressList addressListCorte = null;

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
                            addressListCorte = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayCorte);
                        dv = dvHelper.createValidation(dvConstraint, addressListCorte);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                }
            }
        }catch (Exception e){
            throw new CustomValidatorExcelException("Error al momento de generar el archivo");
        }
        return encabezado;
    }

    private String[] generateListSexo(){
        return new String[] {"Masculino", "Femenino"};
    }
    public String[] generarEncabezadoPagos(String[] pagosFields)throws CustomValidatorExcelException{

        pagosFields[0] = "rfc";
        String[] encabezado = new String[pagosFields.length];

        int rowNum = 0;

        try {

            for (int i = 0; i < pagosFields.length; i++) {
                switch (pagosFields[i]) {
                    case "rfc":
                        encabezado[rowNum++] = "Rfc";
                        break;
                    case "monto":
                        encabezado[rowNum++] = "Monto";
                        break;
                    case "referenciaBancaria":
                        encabezado[rowNum++] = "Referencia Bancaria";
                        break;
                    case "fechaPago":
                        encabezado[rowNum++] = "Fecha de Pago";
                        break;
                }
            }
        }catch (Exception e){
            throw new CustomValidatorExcelException("Error al momento de generar el archivo");
        }
        return encabezado;
    }

    public String[] generarEncabezadoMoneygram(String[] afiliadoFields,String[] relAfiliadoMoneygramFields, XSSFSheet sheet)throws CustomValidatorExcelException{

        String[] encabezado = new String[afiliadoFields.length];

        int rowNum = 0;
        int cellRowNum = 0;

        try {

            for (int i = 0; i < afiliadoFields.length; i++) {
                switch (afiliadoFields[i]) {
                    case "nombre":
                        encabezado[rowNum++] = "Nombre";
                        break;
                    case "apellidoPaterno":
                        encabezado[rowNum++] = "Apellido Paterno";
                        break;
                    case "apellidoMaterno":
                        encabezado[rowNum++] = "Apellido Materno";
                        break;
                    case "fechaNacimiento":
                        encabezado[rowNum++] = "Fecha de Nacimiento";
                        break;
                    case "lugarNacimiento":
                        encabezado[rowNum++] = "Lugar de Nacimiento";
                        break;
                    case "estadoCivil":
                        encabezado[rowNum++] = "Estado Civil";

                        CellRangeAddressList addressListEstadoCivil = null;
                        cellRowNum = rowNum - 1;

                        String [] listEstadoCivil = {"Soltero(a), Casado(a), Viudo(a), Divorciado(a), Desconocido"};

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for(int x  = 1; x < 999; x++){
                            addressListEstadoCivil = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listEstadoCivil);
                        dv = dvHelper.createValidation(dvConstraint, addressListEstadoCivil);

                        dv.setSuppressDropDownArrow(true);
                        sheet.addValidationData(dv);

                        break;
                    case "ocupacion":
                        encabezado[rowNum++] = "Ocupación";
                        break;
                    case "sexo":
                        encabezado[rowNum++] = "Sexo";

                        CellRangeAddressList addressListSexo = null;
                        cellRowNum = rowNum - 1;

                        String[] listSexo = generateListSexo();

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListSexo = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(listSexo);
                        dv = dvHelper.createValidation(dvConstraint, addressListSexo);

                        dv.setSuppressDropDownArrow(true);

                        dv.createPromptBox("Error", "Valod no permitido");
                        dv.setErrorStyle(DataValidation.ErrorStyle.STOP);
                        dv.createErrorBox("Error", "Valor no permitido");

                        sheet.addValidationData(dv);

                        break;
                    case "pais":
                        encabezado[rowNum++] = "País";

                        CellRangeAddressList addressListPais = null;

                        cellRowNum = rowNum - 1;

                        List<Paises> listaPaises = afiliadoService.getAllPaises();
                        String[] arrayPaises = new String[listaPaises.size()];

                        int rowPaises = 0;
                        for (Paises paises : listaPaises) {
                            arrayPaises[rowPaises++] = paises.toString();
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListPais = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayPaises);
                        dv = dvHelper.createValidation(dvConstraint, addressListPais);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);
                        break;
                    case "curp":
                        encabezado[rowNum++] = "CURP";
                        break;
                    case "nss":
                        encabezado[rowNum++] = "NSS";
                        break;
                    case "rfc":
                        encabezado[rowNum++] = "RFC";
                        break;
                    case "telefonoFijo":
                        encabezado[rowNum++] = "Teléfono Fijo";
                        break;
                    case "telefonoMovil":
                        encabezado[rowNum++] = "Teléfono Móvil";
                        break;
                    case "email":
                        encabezado[rowNum++] = "Correo electrónico";
                        break;
                    case "direccion":
                        encabezado[rowNum++] = "Dirección";
                        break;
                    case "municipio":
                        encabezado[rowNum++] = "Municipio";
                        break;
                    case "codigoPostal":
                        encabezado[rowNum++] = "Código Postal";
                        break;
                    case "entidadFederativa":
                        encabezado[rowNum++] = "Entidad Federativa";

                        CellRangeAddressList addressListEstados = null;

                        cellRowNum = rowNum - 1;

                        List<String> listaEstados = afiliadoService.getAllEstados();
                        String[] arrayEstados = new String[listaEstados.size()];

                        int rowEstados = 0;
                        for (String estados : listaEstados) {
                            arrayEstados[rowEstados++] = estados;
                        }

                        dvHelper = new XSSFDataValidationHelper(sheet);

                        for (int x = 1; x < 999; x++) {
                            addressListEstados = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayEstados);
                        dv = dvHelper.createValidation(dvConstraint, addressListEstados);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;
                    case "numeroInfonavit":
                        encabezado[rowNum++] = "Número de infonavit";
                        break;
                    case "fechaAfiliacion":
                        encabezado[rowNum++] = "Fecha de afiliación";
                        break;
                    case "servicio":
                        encabezado[rowNum++] = "Servicio";
                        break;
                    case "periodicidad":
                        encabezado[rowNum++] = "Periodo";
                        break;
                    case "comentarios":
                        encabezado[rowNum++] = "Comentarios";
                        break;
                    case "promotor":
                        encabezado[rowNum++] = "Promotor";
                        break;
                    case "cuenta":
                        encabezado[rowNum++] = "Cuenta Comercial";
                        break;
                    case "corte":
                        encabezado[rowNum++] = "Corte";

                        CellRangeAddressList addressListCorte = null;

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
                            addressListCorte = new CellRangeAddressList(1, x, cellRowNum, cellRowNum);
                        }

                        dvConstraint = dvHelper.createExplicitListConstraint(arrayCorte);
                        dv = dvHelper.createValidation(dvConstraint, addressListCorte);

                        dv.setSuppressDropDownArrow(true);

                        sheet.addValidationData(dv);

                        break;

                }
            }
            
            for (int i = 0; i < relAfiliadoMoneygramFields.length; i++) {
                switch (relAfiliadoMoneygramFields[i]) {
                    case "nombreContratante":
                        encabezado[rowNum++] = "Nombre contratante";
                        break;
                    case "emailContratante":
                        encabezado[rowNum++] = "Correo electrónico Contratante";
                        break;
                    case "telefonoContratante":
                        encabezado[rowNum++] = "Teléfono Contratante";
                        break;
                    
                }
            }
        }catch (Exception e){
            throw new CustomValidatorExcelException("Error al momento de generar el archivo");
        }
        return encabezado;
    }

}
