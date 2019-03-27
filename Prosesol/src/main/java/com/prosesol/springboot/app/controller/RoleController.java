package com.prosesol.springboot.app.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.prosesol.springboot.app.service.IRoleService;

@Controller
@SessionAttributes("perfil")
public class RoleController {

	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IRoleService roleService;
	
	@RequestMapping(value = "/perfiles/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		logger.info("Entra al método de ver perfil");
		
		model.addAttribute("titulo", "Perfiles");
		model.addAttribute("perfil", roleService.findAll());
		
		logger.info("Datos encontrados " + roleService.findAll().toString());
		
		return "catalogos/perfiles/ver";
	}
	
}
