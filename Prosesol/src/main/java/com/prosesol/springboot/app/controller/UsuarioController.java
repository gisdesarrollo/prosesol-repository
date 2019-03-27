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

import com.prosesol.springboot.app.entity.Role;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.service.IRoleService;
import com.prosesol.springboot.app.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IRoleService roleService;
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/usuarios/ver", method = RequestMethod.GET)
	public String ver(Model model) {
				
		logger.info("Entra al método de ver usuario");
		
		model.addAttribute("titulo", "Usuarios");
		model.addAttribute("usuario", usuarioService.findAll());
		
		return "catalogos/usuarios/ver";
	}
	
	@RequestMapping(value = "/usuarios/crear")
	public String crear(Map<String, Object> model) {
		
		logger.info("Entra al método crear usuario");
		
		Usuario usuario = new Usuario();
		
		model.put("usuario", usuario);
		model.put("titulo", "Crear Usuario");
		
		return "catalogos/usuarios/crear";
		
	}
	
	@RequestMapping(value = "/usuarios/crear", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, Model model, SessionStatus status,
						  RedirectAttributes redirect, BindingResult result) {
		
		logger.info("Entra al método guardar usuario");
		
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
	
	@RequestMapping(value = "/usuarios/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		if(id > 0) {
			usuarioService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/usuarios/ver";
	}
	
	@ModelAttribute("perfiles")
	public List<Role> listaPerfiles(){
		return roleService.findAll();
	}
}