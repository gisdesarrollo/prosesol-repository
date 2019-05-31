package com.prosesol.springboot.app.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;

@Controller
@RequestMapping("/cargaMasiva")
public class CargaMasivaController {
	
	protected static final Log logger = LogFactory.getLog(CargaMasivaController.class);
	
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
			
			reportesExcelImpl.generarTemplateAfiliadoXlsx(afiliadoService, periodicidadService, servicioService, 
					  promotorService, cuentaService, response);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			response.setStatus(0);
			
		}		
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadXlsx(@RequestParam("file")MultipartFile fileXlsx, RedirectAttributes redirect, SessionStatus status) {
				
		
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(fileXlsx.getInputStream());
			
			reportesExcelImpl.leerArchivoCargaMasiva(workbook);
			
//			Workbook workbook = new XSSFWorkbook(fileXlsx.getInputStream());
//			StreamingReader reader = StreamingReader.builder()
//									 .rowCacheSize(100)
//									 .bufferSize(4096)
//									 .sheetIndex(0)
//									 .read((InputStream) workbook);
//			
//			for(Row row : reader) {
//				for(Cell cell : row) {
//					System.out.println(cell.getStringCellValue());
//				}
//			}
			
		}catch(IOException io) {
			
			logger.error("Error al momento de leer el archivo", io);
			return "/error/error_500";
		}
		
		return "redirect:/afiliados/ver";
		
	}
	
}
