package com.prosesol.springboot.app.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.exception.CustomValidatorExcelException;

public interface IReportesExcel {

	public void generarTemplateAfiliadoXlsx(HttpServletResponse response);

	public void generarTemplateConciliacionPagosAfiliadoXlsx(HttpServletResponse response);
	
}
