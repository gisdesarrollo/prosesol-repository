package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;
	
	@RequestMapping(value = "/usuarios/ver", method = RequestMethod.GET)
	public String ver(Model model) {
				
		model.addAttribute("titulo", "Usuarios");
		model.addAttribute("usuario", usuarioService.findAll());
		
		return "catalogos/usuarios/ver";
	}
	
	@RequestMapping(value = "/usuarios/crear")
	public String crear(Map<String, Object> model) {
		
		Usuario usuario = new Usuario();
		
		model.put("usuario", usuario);
		model.put("titulo", "Crear Usuario");
		
		return "catalogos/usuario/crear";
		
	}
	
	@RequestMapping(value = "/usuarios/crear", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, Model model, SessionStatus status,
						  RedirectAttributes redirect, BindingResult result) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Usuario");
			return "catalogos/usuarios/crear";
		}
		
		String flashMessage = (usuario.getId() != null) ? "Registro editado con éxito" 
							   : "Registro creado con éxito";
		
		usuario.setPassword(null);
		
		usuarioService.save(usuario);
		redirect.addFlashAttribute("success", flashMessage);
		status.setComplete();
		
		return "redirect:/usuarios/ver";
	}
	
	@RequestMapping(value = "/usuarios/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {
		
		Usuario usuario = null;
		
		if(id > 0) {
			usuario = usuarioService.findById(id);
			if(usuario == null) {
				redirect.addFlashAttribute("Error: ", "El id del usuario no existe");
				return "redirect:/usuarios/ver";
			}
		}else {
			redirect.addFlashAttribute("Error: ", "El id del usuario no puede ser cero");
			return "redirect:/usuarios/ver";
		}
		
		model.put("usuario", usuario);
		model.put("titulo", "Editar usuario");
		
		return "catalogos/usuarios/editar";
	}
}
