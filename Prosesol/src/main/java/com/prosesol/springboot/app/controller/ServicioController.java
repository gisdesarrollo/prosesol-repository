package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IServicioService;

@Controller
@SessionAttributes("servicio")
@RequestMapping("/servicios")
public class ServicioController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IServicioService servicioService;
	
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Servicio servicio = new Servicio();
		
		model.put("servicio", servicio);
		model.put("titulo", "Crear Servicio");
		
		logger.info("Id servicio desde el método de crear: " + servicio.getId());
		
		return "catalogos/servicios/crear";
				
	}
	
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Membresía");
		model.addAttribute("servicios", servicioService.findAll());
		
		return "catalogos/servicios/ver";
	}
	
	
	@Secured("ADMINISTRADOR")
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Servicio servicio, BindingResult result, Model model, 
						 RedirectAttributes redirect, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Membresia");
			return "catalogos/servicios/crear";
		}
		
		String flashMessage = (servicio.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";
		
		servicio.setEstatus(true);
		
		servicioService.save(servicio);
		status.setComplete();
		redirect.addFlashAttribute("success", flashMessage);
		
		logger.info("Id servicio desde el método de guardar: " + servicio.getId());
		
		return "redirect:/servicios/ver";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Servicio servicio = null;

		if (id > 0) {
			servicio = servicioService.findById(id);
			if (servicio == null) {
				redirect.addFlashAttribute("Error: ", "El id del servicio no existe");
				return "redirect:/cuentas/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del servicio no puede ser cero");
			return "redirect:/cuentas/ver";
		}

		model.put("servicio", servicio);
		model.put("titulo", "Editar servicio");

		
		return "catalogos/servicios/editar";
		

	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		if(id > 0) {
			servicioService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/servicios/ver";
	}
	
}
