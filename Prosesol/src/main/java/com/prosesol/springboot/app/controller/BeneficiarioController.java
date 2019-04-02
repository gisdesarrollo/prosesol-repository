package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficiario;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IBeneficiarioService;

@Controller
@SessionAttributes("beneficiario")
@RequestMapping("/beneficiarios")
public class BeneficiarioController {
	
	protected Log logger = LogFactory.getLog(this.getClass());
		
	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private IBeneficiarioService beneficiarioService;
	

	@GetMapping(value = "/crear/{id}")
	public String crear(@PathVariable(value = "id") Long id, Map<String, Object> model,
			RedirectAttributes redirect) {
		
		Beneficiario beneficiario = new Beneficiario();
		Afiliado afiliado = afiliadoService.findById(id);		

		if(afiliado == null) {
			redirect.addFlashAttribute("error", "El id del afiliado no existe");
			return "redirect:/afiliados/ver";
		}
		
		model.put("afiliado", afiliado);
		model.put("beneficiario", beneficiario);
		model.put("titulo", "Crear beneficiario");

		return "catalogos/beneficiarios/crear";
	}

	@RequestMapping(value = "/{id}/crear", method = RequestMethod.POST)
	public String guardar(@Valid Beneficiario beneficiario, @PathVariable(value = "id") 
						 Long id, BindingResult result, Model model, 
						 RedirectAttributes redirect, SessionStatus status) {
		
		System.out.println(beneficiario.toString());
		
		Afiliado afiliado = afiliadoService.findById(id);
		
		if (result.hasErrors()) {
			return "catalogos/afiliados/ver";
		}
		
		beneficiario.addAfiliado(afiliado);
		
		beneficiarioService.save(beneficiario);
		status.setComplete();
		
		return "redirect:/afiliados/ver/";
	}
	
	
}
