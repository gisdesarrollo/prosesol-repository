package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.service.IPeriodicidadService;

@Controller
@SessionAttributes("periodicidad")
@RequestMapping("/periodicidades")
public class PeriodicidadController {

	@Autowired 
	private IPeriodicidadService periodicidadService;
	
	private final Log LOGGER = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		LOGGER.info("Entra al método ver periodo");
		
		model.addAttribute("periodicidad", periodicidadService.findAll());
		
		return "catalogos/periodicidades/ver";
		
	}
	
	@Secured("ADMINISTRADOR")
	@RequestMapping(value = "crear")
	public String crear(Map<String, Object> model) {
		
		Periodicidad periodicidad = new Periodicidad();
		
		model.put("periodos", periodicidadService.getAllEventos());
		model.put("periodicidad", periodicidad);
		
		return "catalogos/periodicidades/crear";
		
	}
	
	@RequestMapping(value = "editar/{id}", method = RequestMethod.POST)
	public String editar(@PathVariable(value = "id") Long id, 
			Map<String, Object> model, RedirectAttributes redirect) {
		
		Periodicidad periodicidad = null;
		
		if(id > 0) {
			periodicidad = periodicidadService.findById(id);
			if(periodicidad == null) {
				LOGGER.error("No se ha podido encontrar la periodicidad en la base de datos");
				return "redirect:/periodicidades/ver";
			}
		}else {
			return "redirect:/periodicidades/ver";
		}
		
		model.put("periodicidad", periodicidad);
		
		return "catalogos/periodicidades/editar";
		
	}
	
	@RequestMapping(value = "crear", method = RequestMethod.POST)
	public String guardar(@Valid Periodicidad periodicidad, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		if(result.hasErrors()) {
			return "catalogos/periodicidades/crear";
		}
		
		
		periodicidadService.save(periodicidad);
		status.setComplete();
		
		return "redirect:/periodicidades/ver";
	}
	
}
