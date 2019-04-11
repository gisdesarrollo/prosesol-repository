package com.prosesol.springboot.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping(value = {"login", "/"})
	public String login(@RequestParam(value = "error", required = false)String error, 
			@RequestParam(value = "logout", required = false)String logout,
			Model model, Principal principal, RedirectAttributes redirect) {
		
		if(principal != null) {
			
			return "home";
		}
		
		if(error != null) {
			model.addAttribute("error", "Error en el login: Nombre de usuario o contrase�a incorrecta");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Ha cerrado sesi�n con �xito");
		}
		return "login";
	}
	
}