package com.prosesol.springboot.app.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.exception.CustomUserException;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;

@Controller
@RequestMapping("/cargaMasiva")
public class CargaMasivaController {
	
	protected static final Log logger = LogFactory.getLog(CargaMasivaController.class);
	
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
	public void descargarTemplate(HttpServletResponse response){
		
		try {
			
			reportesExcelImpl.generarTemplateAfiliadoXlsx(response);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			response.setStatus(0);
			
		}		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadXlsx(@RequestParam("file")MultipartFile fileXlsx, RedirectAttributes redirect, 
							 HttpServletResponse response){				
		
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(fileXlsx.getInputStream());			
			reportesExcelImpl.leerArchivoCargaMasiva(workbook);
			
		}catch(Exception ne) {
			
//			String error = new CustomUserException().getMessage();
//			String[] errorMessage = error.split("\\:");
			
			logger.error("Formato incorrecto", ne);
			redirect.addFlashAttribute("error", "Error al momento de realizar la inserción masiva");
			
			return "redirect:/cargaMasiva/afiliados";
		}
		
		return "redirect:/afiliados/ver";
		
	}
	
}
