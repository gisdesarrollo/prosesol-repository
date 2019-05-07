package com.prosesol.springboot.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.prosesol.springboot.app.entity.Correo;

@Controller
@SessionAttributes("correo")
@RequestMapping("/correos")
public class EmailController {
	

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	@RequestMapping(value = "/crear")
	public String crear(Model model) {
		
		LOGGER.info("Ingresar al método crear correo");
		
		model.addAttribute("correo", new Correo());
				
		return "/catalogos/correos/crear";
		
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("correo") Correo correo, BindingResult result,
						  Model model, SessionStatus status) {
		
		
		
		if(correo.getHtml().isEmpty()) {
			System.out.println("No hay datos qué mostrar");
		}
		
		try {
			
			if(result.hasErrors()) {
				return "/errores/error_500";
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
	
}
