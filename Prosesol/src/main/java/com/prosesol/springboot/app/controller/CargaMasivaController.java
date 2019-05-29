package com.prosesol.springboot.app.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;

@Controller
@RequestMapping("/cargaMasiva")
public class CargaMasivaController {
	
	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private ReportesExcelImpl reportesExcelImpl;

	@GetMapping("/afiliados")
	public String cargaMasiva() {
		return "cargaMasiva/afiliados";
	}
	
	/**
	 * Descargar Template Excel
	 */
	
	@Secured("ROLE_ADMINISTRADOR")
	@GetMapping("/templateXlsx")
	public void descargarTemplate(HttpServletResponse response)throws Exception{
		reportesExcelImpl.generarTemplateAfiliadoXlsx(afiliadoService, response);
	}
	
}
