package com.prosesol.springboot.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.util.Eventos;

@Controller
@SessionAttributes("periodicidad")
@RequestMapping("/periodicidades")
public class PeriodicidadController {

	@Autowired 
	private IPeriodicidadService periodicidadService;
	
	private final Log LOGGER = LogFactory.getLog(this.getClass());
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		LOGGER.info("Entra al método ver periodo");
		
		model.addAttribute("periodicidad", periodicidadService.findAll());
		
		return "catalogos/periodicidades/ver";
		
	}
	
	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Periodicidad periodicidad = new Periodicidad();
		
		model.put("periodicidad", periodicidad);
		
		return "catalogos/periodicidades/crear";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/editar/{id}")
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
		model.put("id", id);
		return "catalogos/periodicidades/editar";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "crear", method = RequestMethod.POST)
	public String guardar(@Valid Periodicidad periodicidad, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		String messageStatus = "";
		
		try {
			
			if(result.hasErrors()) {
					return "catalogos/periodicidades/crear";
			}
								
			messageStatus = (periodicidad.getId() == null) ? "El periodo se ha creado correctamente" : "El periodo se ha editado correctamente";
			
			redirect.addFlashAttribute("success", messageStatus);
			periodicidadService.save(periodicidad);
			status.setComplete();
			
			
		}catch(Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un error en el sistema, contacte al administrador");
			
			return "redirect:/periodicidades/ver";
		}
		
		return "redirect:/periodicidades/ver";
	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		try {
			
			if(id > 0) {
				periodicidadService.deleteById(id);
			}else {
				redirect.addFlashAttribute("error", "El id no puede ser cero");
				return "catalogos/periodicidades/ver";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");
			
			return "catalogos/periodicidades/ver";
		}
		
		redirect.addFlashAttribute("success", "El periodo se ha eliminado correctamente");
		
		return "redirect:/periodicidades/ver";
		
	}
	
	@ModelAttribute(name = "periodos")
	public List<Eventos> getAllPeriodos(){
		return periodicidadService.getAllEventos();
	}
	
}
