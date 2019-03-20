package com.prosesol.springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

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
	
}
