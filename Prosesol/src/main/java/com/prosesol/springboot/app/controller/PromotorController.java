package com.prosesol.springboot.app.controller;

import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.service.IPromotorService;

@Controller
@SessionAttributes("promotor")
@RequestMapping("/promotores")
public class PromotorController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IPromotorService promotorService;
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Promotores");
		model.addAttribute("promotor", promotorService.findAll());
		
		return "catalogos/promotores/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Promotor promotor = new Promotor();
		
		model.put("promotor", promotor);
		model.put("titulo", "Crear Promotor");
		
		return "catalogos/promotores/crear";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Promotor promotor, BindingResult result, Model model, RedirectAttributes redirect,
						 SessionStatus status) {
		
		try {
			if(result.hasErrors()) {
				model.addAttribute("titulo", "Crear Promotor");
				return "catalogos/promotores/crear";
			}
			
			String flashMessage = (promotor.getId() != null) ? "Promotor editado con éxito" : "Promotor creado con éxito";
			
			promotor.setEstatus(true);
			promotor.setFechaAlta(new Date());
			
			promotorService.save(promotor);
			status.setComplete();
			redirect.addFlashAttribute("success", flashMessage);
		}catch(Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");
			
			return "redirect:/promotores/ver";
		}
		
		return "redirect:/promotores/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Promotor promotor = null;

		if (id > 0) {
			promotor = promotorService.findById(id);
			if (promotor == null) {
				redirect.addFlashAttribute("Error: ", "El id del promotor no existe");
				return "redirect:/promotores/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del promotor no puede ser cero");
			return "redirect:/promotores/ver";
		}

		model.put("promotor", promotor);
		
		return "catalogos/promotores/editar";
		

	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		try {
			
			if(id > 0) {
				promotorService.delete(id);
				redirect.addFlashAttribute("success", "Registro eliminado correctamente");
			}
		}catch(DataIntegrityViolationException dive) {
			dive.printStackTrace();
			
			redirect.addFlashAttribute("error", "No se puede eliminar el promotor porque está asociado a uno o más afiliados");
			
			return "redirect:/promotores/ver";
		}catch(Exception e) {
			
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un error en el sistema, contacte al administrador");
			
			return "redirect:/promotores/ver";
			
		}
		
		return "redirect:/promotores/ver";
	}
	
}
