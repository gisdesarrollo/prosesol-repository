package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

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

import com.prosesol.springboot.app.entity.Membresia;
import com.prosesol.springboot.app.service.IMembresiaService;

@Controller
@SessionAttributes("membresia")
public class MembresiaController {

	@Autowired
	private IMembresiaService membresiaService;
	
	@RequestMapping(value = "/membresias/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Membresía");
		model.addAttribute("membresia", membresiaService.findAll());
		
		return "catalogos/membresias/ver";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/membresias/crear")
	public String crear(Map<String, Object> model) {
		
		Membresia membresia = new Membresia();
		
		model.put("membresia", membresia);
		model.put("titulo", "Crear Membresia");
		
		return "catalogos/membresias/crear";
				
	}
	
	@RequestMapping(value = "/membresias/crear", method = RequestMethod.POST)
	public String guardar(@Valid Membresia membresia, BindingResult result, Model model, 
						 RedirectAttributes redirect, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Membresia");
			return "catalogos/membresias/crear";
		}
		
		String flashMessage = (membresia.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";
		
		membresiaService.save(membresia);
		status.setComplete();
		redirect.addFlashAttribute("success", flashMessage);
		
		return "redirect:/membresias/ver";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/membresias/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Membresia membresia = null;

		if (id > 0) {
			membresia = membresiaService.findById(id);
			if (membresia == null) {
				redirect.addFlashAttribute("Error: ", "El id de la membresia no existe");
				return "redirect:/cuentas/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id de la membresia no puede ser cero");
			return "redirect:/cuentas/ver";
		}

		model.put("membresia", membresia);
		model.put("titulo", "Editar membresía");

		
		return "catalogos/membresias/editar";
		

	}
	
	@RequestMapping(value = "/membresias/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		if(id > 0) {
			membresiaService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/membresias/ver";
	}
	
}