package com.prosesol.springboot.app.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICandidatoService;
import com.prosesol.springboot.app.service.IIncidenciaService;
import com.prosesol.springboot.app.service.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private ICandidatoService candidatoService;

	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IIncidenciaService incidenciaService;

	private String url;
	
	@GetMapping(value = {"login", "/home", "/"})
	public String login(@RequestParam(value = "error", required = false)String error, 
			@RequestParam(value = "logout", required = false)String logout,
			Model model,  Principal principal) {
				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(principal != null) {
			
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			url = null;
			
			authorities.forEach(authority ->{
				if(authority.getAuthority().equals("ROLE_ADMINISTRADOR") || authority.getAuthority().equals("ROLE_USUARIO")) {
					url = "/home";
				}else if(authority.getAuthority().equals("ROLE_ASISTENCIA")) {
					url = "/incidencias/home";
				}
			});
			
			return url;
		}
		
		if(error != null) {
			model.addAttribute("error", "Error en el login: Nombre de usuario o contrase�a incorrecta");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Ha cerrado sesi�n con �xito");
		}
		return "login";
	}

	@ModelAttribute("totalAfiliados")
	private Integer getAllAfiliados(){
		return afiliadoService.getTotalAfiliados();
	}

	@ModelAttribute("totalCandidatos")
	private Integer getAllCandidatos(){
		return candidatoService.findAll().size();
	}

	@ModelAttribute("totalServicios")
	private Integer getAllServicios(){
		return servicioService.findAll().size();
	}

	@ModelAttribute("activas")
	private Integer getAllIncidenciasActivas(){
		return incidenciaService.getAllIncidenciasACtivas();
	}

	@ModelAttribute("enProceso")
	private Integer getAllIncidenciasEnProceso(){
		return  incidenciaService.getAllIncidenciasEnProceso();
	}

	@ModelAttribute("completadas")
	private Integer getAllIncidenciasCompletadas(){
		return incidenciaService.getAllIncidenciasCompletadas();
	}

	@ModelAttribute("canceladas")
	private Integer getAllIncidenciasCanceladas(){
		return incidenciaService.getAllIncidenciasCanceladas();
	}
}