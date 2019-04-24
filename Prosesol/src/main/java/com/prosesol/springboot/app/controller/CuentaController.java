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

import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.service.ICuentaService;

@Controller
@SessionAttributes("cuenta")
public class CuentaController {

	@Autowired
	private ICuentaService cuentaService;
	
	@RequestMapping(value = "/cuentas/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Cuentas");
		model.addAttribute("cuenta", cuentaService.findAll());
		
		return "catalogos/cuentas/ver";
	}
	
	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "cuentas/crear")
	public String crear(Map<String, Object> model) {
		Cuenta cuenta = new Cuenta();

		model.put("cuenta", cuenta);
		model.put("titulo", "Crear Cuenta");
	
		return "catalogos/cuentas/crear";
	}
	
	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "cuentas/crear", method = RequestMethod.POST)
	public String guardar(@Valid Cuenta cuenta, BindingResult result, Model model, RedirectAttributes redirect,
						  SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Cuenta");
			return "/catalogos/cuentas/crear";
		}
		
		String flashMessage = (cuenta.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";
		
		cuenta.setEstatus("Activo");
		
		cuentaService.save(cuenta);
		status.setComplete();
		redirect.addFlashAttribute("success", flashMessage);
		
		return "redirect:/cuentas/ver";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	@RequestMapping(value = "/cuentas/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Cuenta cuenta = null;

		if (id > 0) {
			cuenta = cuentaService.findById(id);
			if (cuenta == null) {
				redirect.addFlashAttribute("Error: ", "El id de la cuenta no existe");
				return "redirect:/cuentas/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id de la cuenta no puede ser cero");
			return "redirect:/cuentas/ver";
		}

		model.put("cuenta", cuenta);
		model.put("titulo", "Editar cuenta");

		
		return "catalogos/cuentas/editar";
		

	}
	
	@RequestMapping(value = "/cuentas/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		if(id > 0) {
			cuentaService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/cuentas/ver";
	}
	
}
