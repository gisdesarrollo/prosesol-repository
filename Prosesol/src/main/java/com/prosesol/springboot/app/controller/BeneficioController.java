package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.service.IBeneficioService;

@Controller
@RequestMapping("/beneficios")
@SessionAttributes("beneficio")
public class BeneficioController {

	protected final static Logger LOGGER = LoggerFactory.getLogger(Beneficio.class);
	
	@Autowired
	private IBeneficioService beneficioService;
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		LOGGER.info("Muestra los beneficios guardados");
		
		model.addAttribute("beneficios", beneficioService.findAll());
		
		return "/catalogos/beneficios/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Beneficio beneficio = new Beneficio();
		
		model.put("beneficio", beneficio);
		
		return "catalogos/beneficios/crear";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Beneficio beneficio, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		LOGGER.info("Guardar Beneficio");
	
		String flashMessage = (beneficio.getId() != null) ? "Registro editado correctamente" : "Registro creado correctamente";
		
		try {
			beneficioService.save(beneficio);
			status.setComplete();
			redirect.addFlashAttribute("success", flashMessage);
			
			LOGGER.info("El beneficio se ha guardado correctamente");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/beneficios/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable("id")Long id, Map<String, Object> model, RedirectAttributes redirect) {
		
		LOGGER.info("Editar Beneficio");
		
		Beneficio beneficio = null;
		
		if(id > 0) {
			beneficio = beneficioService.findById(id);
			if(beneficio == null) {
				redirect.addFlashAttribute("error", "El id del beneficio no existe");
				LOGGER.info("El id del beneficio no existe: " + id);				
				return "redirect:/beneficios/ver";
			}
		}else {
			redirect.addFlashAttribute("error", "El id no puede ser cero");
			LOGGER.info("El id no puede ser cero");
			return "redirect:/beneficios/ver";
		}		
		
		model.put("beneficio", beneficio);
		
		LOGGER.info("Registro: " + beneficio.getNombre() + " editado correctamente");
		
		return "catalogos/beneficios/editar";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable("id")Long id, RedirectAttributes redirect) {
		
		LOGGER.info("Registro a eliminar: " + beneficioService.findById(id));
		
		if(id > 0) {
			beneficioService.deleteById(id);
			LOGGER.info("Registro eliminado correctamente");
			
		}else {
			redirect.addFlashAttribute("error", "El id no puede ser cero");
			
			LOGGER.info("El id no puede ser cero");
			return "redirect:/beneficios/ver";
		}
			
		redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		
		return "redirect:/beneficios/ver";
	}
	
}
