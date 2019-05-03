package com.prosesol.springboot.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.service.IPerfilService;
import com.prosesol.springboot.app.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/usuarios")
public class UsuarioController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Value("${app.password}")
	private String password;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IPerfilService perfilService;
	
	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
				
		logger.info("Entra al método de ver usuario");
		
		model.addAttribute("titulo", "Usuarios");
		model.addAttribute("usuario", usuarioService.findAll());
		
		return "catalogos/usuarios/ver";
	}
	
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		logger.info("Entra al método crear usuario");
		
		Usuario usuario = new Usuario();
		
		model.put("usuario", usuario);
		model.put("titulo", "Crear Usuario");
		
		return "catalogos/usuarios/crear";
		
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@ModelAttribute("perfiles")Perfil perfiles, @Valid Usuario usuario, 
						  BindingResult result, Model model, RedirectAttributes redirect, SessionStatus status) {
		
		String passwordUser = null;	
		Perfil perfil = perfilService.findById(perfiles.getId());
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Usuario");
			return "catalogos/usuarios/crear";
		}
		
		if(usuario.getId() != null) {
			logger.info("Registro: " + usuario.getNombre() + " editado con éxito");
		}else {
			
			usuario.setEstatus(true);
			logger.info("Registro creado con éxito");
			
			for(int i = 0;i < 2; i++) {
				passwordUser = passwordEncoder.encode(password);
			}
			
			usuario.getPerfiles().add(perfil);
			usuario.setPassword(passwordUser);
			
		}
		
		usuarioService.save(usuario);
		status.setComplete();
		
		return "redirect:/usuarios/ver";
	}
	
	@RequestMapping(value = "/editar/{id}")
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
	
	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		if(id > 0) {
			usuarioService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/usuarios/ver";
	}
	
	/**
	 * Método para mostrar los perfiles
	 * Dentro del list box de crear usuario 
	 */
	
	@ModelAttribute("perfiles")
	public List<Perfil> listaPerfiles(){
		return perfilService.findAll();
	}
}
