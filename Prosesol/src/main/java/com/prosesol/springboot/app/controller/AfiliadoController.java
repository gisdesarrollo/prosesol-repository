package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;

@Controller
@SessionAttributes("afiliado")
public class AfiliadoController {

	@Autowired
	private IAfiliadoService afiliadoService;
	
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {
		
		Afiliado afiliado = new Afiliado();
		
		model.put("afiliado", afiliado);
		model.put("titulo", "Crear Afiliado");
		
		return "catalogos/afiliados/crear";
	}
	
	@RequestMapping(value="/crear/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, 
						RedirectAttributes redirect) {
		
		Afiliado afiliado = null;
		
		if(id > 0) {
			afiliado = afiliadoService.findById(id);
			if(afiliado == null) {
				redirect.addFlashAttribute("Error: ", "El id del afiliado no existe");
				return "redirect:/ver";
			}
		}else {
			redirect.addFlashAttribute("Error: ", "El id del afiliado no puede ser cero");
			return "redirect:/ver";
		}
		
		model.put("afiliado", afiliado);
		model.put("titulo", "Editar afiliado");
		
		return "catalogos/afiliados/crear";
		
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model,
						RedirectAttributes redirect, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Afiliado");
			return "catalogos/afiliados/crear";
		}
		
		String mensajeFlash = (afiliado.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";
		
		afiliadoService.save(afiliado);
		status.setComplete();
		redirect.addFlashAttribute("success", mensajeFlash);
		
		return "redirect:/ver";		
	}
	 
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {
		
		model.addAttribute("titulo", "Afiliados");
		model.addAttribute("afiliado", afiliadoService.findAll());
		
		return "catalogos/afiliados/ver";
		
	}
	
}
