package com.prosesol.springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.prosesol.springboot.app.service.CuentaServiceImpl;

@Controller
@SessionAttributes("cuenta")
public class CuentaController {

	@Autowired
	private CuentaServiceImpl cuentaService;
	
	@RequestMapping(value = "/cuentas/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Cuentas");
		model.addAttribute("cuenta", cuentaService.findAll());
		
		return "catalogos/cuentas/ver";
	}
	
}
