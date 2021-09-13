package com.prosesol.springboot.app.service;

import javax.servlet.http.HttpServletResponse;


public interface IReportesExcel {

	public void generarTemplateAfiliadoXlsx(HttpServletResponse response);
	
	public void generarTemplateConciliacionPagosAfiliadoXlsx(HttpServletResponse response);

	void generarTemplateAfiliadoMoneygramXlsx(HttpServletResponse response);
}
