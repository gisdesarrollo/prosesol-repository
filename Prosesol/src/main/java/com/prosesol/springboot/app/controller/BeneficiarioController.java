package com.prosesol.springboot.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.Paises;

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
	
	@Autowired
	private IPromotorService promotorService;
	
	@Autowired
	private ICuentaService cuentaService;
	
	private static Long idAfiliado;
	
	@RequestMapping(value = "/crear/{id}", method = RequestMethod.GET)
	public String crear(@PathVariable("id")Long id, Map<String, Object> model) {
		
		idAfiliado = id;
		
		Afiliado beneficiario = new Afiliado();
		
		model.put("beneficiario", beneficiario);
		model.put("titulo", "Crear Beneficiario");

		return "catalogos/beneficiarios/crear";
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		System.out.println(idAfiliado);
		
		System.out.println("Guardar beneficiario");
		
		if(result.hasErrors()) {
			System.out.println("Error en el proceso");
			model.addAttribute("titulo", "Crear Beneficiario");
			return "catalogos/beneficiarios/crear";
		}
		
//		String flashMessage = (afiliado.getId() != null) ? "Registro creado con éxito" : "Registro editado con éxito";
		
		afiliado.setSaldoAcumulado(afiliado.getServicio().getCosto());
		afiliado.setIsBeneficiario(true);
		afiliado.setEstatus(true);
		
		afiliadoService.save(afiliado);
		guardarRelAfiliadoBeneficiario(afiliado, idAfiliado);
		status.setComplete();
		
		return "redirect:/afiliados/detalle/" + idAfiliado;
		
	}
	
	public void guardarRelAfiliadoBeneficiario(Afiliado beneficiario, Long id) {		
		afiliadoService.insertBeneficiarioUsingJpa(beneficiario, id);		
	}
	
	/**
	 * Método para mostrar los periodos Dentro del list box de crear afiliados
	 * 
	 * @param(name = ModelAttribute)
	 */

	@ModelAttribute("periodos")
	public List<Periodicidad> listaPeriodos() {
		return periodicidadService.findAll();
	}

	/**
	 * Método para mostrar los estados Dentro del list box de crear afiliados
	 * 
	 * @param(name = "ModelAttribute")
	 */

	@ModelAttribute("estados")
	public List<String> getAllEstados() {
		return afiliadoService.getAllEstados();
	}

	/**
	 * Método para mostrar los países Dentro del list box de crear afiliados
	 * 
	 * @param(name = "ModelAttribute")
	 */

	@ModelAttribute("paises")
	public List<Paises> getAllPaises() {
		return afiliadoService.getAllPaises();
	}

	/**
	 * Método para mostrar los servicios Dentro del list box de crear afiliados
	 * 
	 * @param(name = "ModelAttribute")
	 */

	@ModelAttribute("servicios")
	public List<Servicio> getAllServicios() {
		return servicioService.findAll();
	}
	/**
	 * Método para mostrar los servicios Dentro del list box de crear afiliados
	 * 
	 * @param(name = "promotores")
	 */
	
	@ModelAttribute("promotores")
	public List<Promotor> getAllPromotores(){
		return promotorService.findAll();
	}
	
	/**
	 * Método para mostrar los servicios Dentro del list box de crear afiliados
	 * 
	 * @param(name = "cuentas")
	 */
	
	@ModelAttribute("cuentas")
	public List<Cuenta> getAllCuentas(){
		return cuentaService.findAll();
	}
}
