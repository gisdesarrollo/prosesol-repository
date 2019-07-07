package com.prosesol.springboot.app.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.Paises;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/beneficiarios")
public class BeneficiarioController {

	protected Log logger = LogFactory.getLog(BeneficiarioController.class);
	
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
	
	@Autowired
	private CalcularFecha calcularFechas;
	
	private static Long idAfiliado;
	
	@RequestMapping(value = "/crear/{id}")
	public String crear(@PathVariable("id")Long id, Map<String, Object> model) {
		
		idAfiliado = id;
		System.out.println(idAfiliado);
		
		Afiliado beneficiario = new Afiliado();
		
		model.put("afiliado", beneficiario);
		model.put("titulo", "Crear Beneficiario");

		return "catalogos/beneficiarios/crear";
	}
	
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@ModelAttribute("clave") String clave,
						  @Valid Afiliado afiliado, BindingResult result, Model model, 
						  RedirectAttributes redirect, SessionStatus status) {
		
		System.out.println(idAfiliado);
		
		Date fechaAlta = new Date();
//		Periodicidad periodicidad = new Periodicidad();		
		try {
			
			if(result.hasErrors()) {
				System.out.println("Error en el proceso");
				model.addAttribute("titulo", "Crear Beneficiario");
				return "catalogos/beneficiarios/crear";
			}
			
//			periodicidad = periodicidadService.findById(afiliado.getPeriodicidad().getId());
//			
//			Date fechaCorte = calcularFechas.calcularFechas(periodicidad, afiliado.getCorte());
//			
//			afiliado.setFechaCorte(fechaCorte);
			afiliado.setEstatus(3);
//			afiliado.setSaldoAcumulado(afiliado.getServicio().getCosto());
			afiliado.setIsBeneficiario(true);
			afiliado.setClave(clave);
			afiliado.setFechaAlta(fechaAlta);
			
			
			afiliadoService.save(afiliado);
			guardarRelAfiliadoBeneficiario(afiliado, idAfiliado);
			status.setComplete();
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);
			return "/error/error_500";
		}
		
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
	
	/**
	 * Método para asignar una clave para el Afiliado
	 * 
	 * @param(name = "clave")
	 */
	
	@ModelAttribute("clave")
	public String getClaveAfiliado() {
		
		String clave = "0123456789";
		String claveAfiliado = "";
		
		for(int i = 0; i < 10; i++) {
			claveAfiliado += (clave.charAt((int)(Math.random() * clave.length())));
		}
		
		return claveAfiliado;
	}
}
