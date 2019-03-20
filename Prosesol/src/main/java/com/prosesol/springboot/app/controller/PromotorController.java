package com.prosesol.springboot.app.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.prosesol.springboot.app.service.IPromotorService;

@Controller
@SessionAttributes("promotor")
public class PromotorController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IPromotorService promotorService;
	
	@RequestMapping(value = "/promotores/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Promotores");
		model.addAttribute("promotor", promotorService.findAll());
		
		return "catalogos/promotores/ver";
	}
}
