package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.service.IPerfilService;
import com.prosesol.springboot.app.service.IRoleService;

@Controller
@SessionAttributes("perfil")
@RequestMapping("/perfiles")
public class PerfilController {

	protected Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IPerfilService perfilService;
	
	@Autowired
	private IRoleService roleService;
	
	@GetMapping(value = "/ver")
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Perfiles");
		model.addAttribute("perfil", perfilService.findAll());
		
		logger.info("Datos encontrados " + perfilService.findAll().toString());
		
		return "catalogos/perfiles/ver";
		
	}
	
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Perfil perfil = new Perfil();
		
		model.put("titulo", "Crear Perfil");
		model.put("roles", roleService.findAll());
		model.put("perfil", perfil);
		
		return "catalogos/perfiles/crear";
		
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Perfil perfil, BindingResult result, Model model, RedirectAttributes redirect,
						  SessionStatus status) {
		
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Perfil");
			return "catalogos/perfiles/crear";
		}
		
		if(perfil.getId() != null) {
			logger.info("Perfil editado con éxito");
		}else {
			perfil.setEstatus(true);
			logger.info("Perfil creado con éxito");
		}	
		
		perfilService.save(perfil);
		status.setComplete();
		
		return "redirect:/perfiles/ver";
		
		
	}
	
	@RequestMapping(value = "/editar/{id}")
	private String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {
		
		Perfil perfil = null;
		
		if(id > 0) {
			perfil = perfilService.findById(id);
			if(perfil == null) {
				model.put("error", "El perfil no existe, ingrese otro id");
				return "redirect:/perfiles/ver";
			}
		}else {
			model.put("error", "No existe el perfil con id cero");
		}
		
		model.put("titulo", "Editar perfil");
		model.put("roles", roleService.findAll());
		model.put("perfil", perfil);
		
		return "catalogos/perfiles/editar";
		
	}
	
	@RequestMapping(value = "/borrar/{id}")
	public String borrar(@PathVariable("id")Long id, RedirectAttributes redirect) {
		
		logger.info("Id de Perfil: " + id);
		
		if(id > 0) {
			
			perfilService.deleteById(id);			
			logger.info("El perfil se ha borrado correctamente");
		}
		
		return "redirect:/perfiles/ver";
	}
	
}
