package com.prosesol.springboot.app.controller;

import javax.validation.Valid;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.prosesol.springboot.app.entity.Correo;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICorreoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;

@Controller
@SessionAttributes("correo")
@RequestMapping("/correos")
public class EmailController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);
	
	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private IServicioService servicioServicio;
	
	@Autowired
	private IPromotorService promotorService;
	
	@Autowired
	private ICuentaService cuentaService;
	
	@Autowired
	private IPeriodicidadService periodoService;
	
	@Autowired
	private ICorreoService correoService;

	@RequestMapping(value = "/crear")
	public String crear(Model model) {
				
		LOGGER.info("Ingresar al método crear correo");
		
		model.addAttribute("correo", new Correo());
				
		return "/catalogos/correos/crear";
		
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Correo correo,
				          BindingResult result,
						  Model model, SessionStatus status) {
		
		if(correo.getHtml().isEmpty()) {
			System.out.println("No hay datos qué mostrar");
		}
		
		Correo template = correoService.getTemplateCorreoByName("template");
		Document doc = Jsoup.parse(template.getHtml());
		Elements td = doc.select("td");
		
		for(Element p : td) {
			if(!p.hasAttr("align")) {				
				p.append(correo.getHtml());
				System.out.println(p);
			}
		}
		
		System.out.println(doc.html());
		
		try {
			
			if(result.hasErrors()) {
				return "/error/error_500";
			}
			
			System.out.println(correo.getHtml());
			
		}catch(Exception e) {
			LOGGER.error("Error al momento de ejecutar el proceso", e.getMessage());
			return "/errores/error_500";
		}
		
		return "redirect:/correos/crear";
		
	}
	
	@RequestMapping(value = "/ver")
	public String ver(Model model) {
		
		return "catalogos/correos/ver";
		
	}
	
	/**
	 * Método para obtener las variables para el Objeto de Afiliado
	 * @return("variablesAfiliado")
	 */
	
	@ModelAttribute("variablesAfiliado")
	public String[] getVariablesAfiliado() {
		
		return afiliadoService.getVariablesAfiliado();
		
	}
	
	/**
	 * Método para obtener las variables para el Objeto de Servicio
	 * @return("variablesServicio")
	 */
	
	@ModelAttribute("variablesServicio")
	public String[] getVariablesServicio() {
		
		return servicioServicio.getVariablesServicio();
		
	}
	
	/**
	 * Método para obtener las variables para el Objeto de Promotor
	 * @return("variablesPromotor")
	 */
	
	@ModelAttribute("variablesPromotor")
	public String[] getVariablesPromotor() {
		
		return promotorService.getVariablesPromotor();
		
	}
	
	/**
	 * Método para obtener las variables para el Objeto de Cuenta
	 * @return("variablesCuenta")
	 */
	
	@ModelAttribute("variablesCuenta")
	public String[] getVariablesCuenta() {
		return cuentaService.getVariablesCuenta();
	}
	
	/**
	 * Método para obtener las variables para el Objeto de Periodo
	 * @return("variablesPeriodo")
	 */
	
	@ModelAttribute("variablesPeriodo")
	public String[] getVariablesPeriodo() {
		return periodoService.getVariablesPeriodo();
	}
}
