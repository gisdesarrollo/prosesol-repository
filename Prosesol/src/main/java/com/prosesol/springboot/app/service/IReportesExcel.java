package com.prosesol.springboot.app.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IReportesExcel {

	public void generarReporteAfiliadoXlsx(List<Afiliado> afiliados, HttpServletResponse response);
	
	public void generarTemplateAfiliadoXlsx(HttpServletResponse response);
	
}
