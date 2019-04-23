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
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IServicioService;

@Controller
@SessionAttributes("beneficiario")
@RequestMapping("/beneficiarios")
public class BeneficiarioController {

	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IPeriodicidadService periodicidadService;
	
	private static Long idAfiliado;
	
	@RequestMapping(value = "/crear/{id}", method = RequestMethod.GET)
	public String crear(@PathVariable("id")Long id, Map<String, Object> model) {
		
		idAfiliado = id;
		
		Afiliado beneficiario = new Afiliado();
		
		model.put("beneficiario", beneficiario);
		model.put("estados", afiliadoService.getAllEstados());
		model.put("paises", afiliadoService.getAllPaises());
		model.put("servicios", servicioService.findAll());
		model.put("periodos", periodicidadService.findAll());
		model.put("titulo", "Crear Beneficiario");

		return "catalogos/beneficiarios/crear";
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		System.out.println(idAfiliado);
		
		System.out.println("Guardar beneficiario");
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Beneficiario");
			return "catalogos/beneficiarios/crear";
		}
		
		String flashMessage = (afiliado.getId() != null) ? "Registro creado con éxito" : "Registro editado con éxito";
		
		afiliado.setIsBeneficiario(true);
		afiliado.setEstatus(true);
		
		afiliadoService.save(afiliado);
		guardarRelAfiliadoBeneficiario(afiliado, idAfiliado);
		status.setComplete();
		redirect.addFlashAttribute("success", flashMessage);
		
		return "redirect:/afiliados/detalle/" + idAfiliado;
		
	}
	
	public void guardarRelAfiliadoBeneficiario(Afiliado beneficiario, Long id) {		
		afiliadoService.insertBeneficiarioUsingJpa(beneficiario, id);		
	}
	
}
