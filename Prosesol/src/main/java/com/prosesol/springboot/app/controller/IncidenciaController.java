package com.prosesol.springboot.app.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Incidencia;
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
	
	@RequestMapping(value = "/buscar")
	public String crear(Model model) {
		
		Afiliado afiliado = new Afiliado();
		
		model.addAttribute("afiliado", afiliado);
		
		return "incidencias/buscar";
	}
	
	@RequestMapping(value = "/afiliados", method = RequestMethod.POST)
	public String buscar(@RequestParam(name = "campos[]") String[] campos,
				         Model model, RedirectAttributes redirect)throws Exception {
		
		List<AfiliadoCustom> afiliados = afiliadoService.getAfiliadoByParams(campos);
				
		if(afiliados.size() == 0) {
			redirect.addFlashAttribute("error", "Registro no encontrado");
			return "redirect:/incidencias/buscar";
		}
		
		model.addAttribute("afiliados", afiliados);
		
		return "/incidencias/busqueda";
	}
	
	@RequestMapping(value = "/crear/{id}")
	public String crear(@PathVariable("id")Long id, Model model) {
		
		Afiliado afiliado = afiliadoService.findById(id);
		Incidencia incidencia = new Incidencia();
		
		model.addAttribute("afiliado", afiliado);
		model.addAttribute("incidencia", incidencia);
		
		return "incidencias/crear";
	}
	
}
