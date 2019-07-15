package com.prosesol.springboot.app.controller;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IIncidenciaService;

@Controller
@RequestMapping("/incidencias")
@SessionAttributes("incidencia")
public class IncidenciaController {

	protected static final Log LOG = LogFactory.getLog(IncidenciaController.class);  
	
	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private IIncidenciaService incidenciaService;
	
	@RequestMapping(value = "/crear")
	public String crear(Model model) {
		
		Afiliado afiliado = new Afiliado();
		
		model.addAttribute("afiliado", afiliado);
		
		return "incidencias/crear";
	}
	
	@RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public String buscar(@RequestParam(name = "campos[]") String[] campos,
				         Model model, RedirectAttributes redirect)throws Exception {
				
		String[] cadena = new String[campos.length];
//		AfiliadoCustom buscarAfiliado = afiliadoService.findAfiliadoByClave(clave);
		
//		if(buscarAfiliado == null) {
//			redirect.addFlashAttribute("error", "Afiliado no encontrado");
//			return "incidencias/crear";
//		}
		for(int i = 0; i < campos.length; i++) {
			if(campos[i].equals("")) {
				continue;
			}else {
				System.out.println(campos[i]);
			}
		}
		
		
		return "redirect:/incidencias/descripcion/";
	}
	
	@RequestMapping(value = "/descripcion/{clave}")
	public String descripcion(@PathVariable(value = "clave")String clave, Model model,
							  RedirectAttributes redirect) {
		
		AfiliadoCustom afiliado = afiliadoService.findAfiliadoByClave(clave);
		
		model.addAttribute("afiliado", afiliado);
		
		return "incidencias/descripcion";
		
	}
}
