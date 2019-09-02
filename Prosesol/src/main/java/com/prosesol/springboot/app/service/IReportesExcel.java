package com.prosesol.springboot.app.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IReportesExcel {

	public void generarTemplateAfiliadoXlsx(HttpServletResponse response);
	
}
