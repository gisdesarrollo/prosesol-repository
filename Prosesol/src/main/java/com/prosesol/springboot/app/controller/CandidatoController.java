package com.prosesol.springboot.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Candidato;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.ICandidatoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.Paises;

@Controller
@RequestMapping("candidatos")
@SessionAttributes(value = "candidato")
public class CandidatoController {

	@Autowired
	private ICandidatoService candidatoService;
	
	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IPeriodicidadService periodicidadService;

	@Autowired
	private IPromotorService promotorService;

	@Autowired
	private ICuentaService cuentaService;
	
	
	@GetMapping(value = "/ver")
	public String ver(Model model) {
		
		model.addAttribute("candidatos", candidatoService.findAll());
		
		return "catalogos/candidatos/ver";
	}
	
	@GetMapping(value = "/crear")
	public String crear(Model model) {
		
		Candidato candidato = new Candidato();
		model.addAttribute("candidato", candidato);
		
		return "catalogos/candidatos/crear";
	}
	
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable("id")Long id, Model model, RedirectAttributes redirect) {
		
		Candidato candidato = null;
		
		try {
			
			if(id > 0) {
				candidato = candidatoService.findById(id);
				if(candidato == null) {
					
					redirect.addFlashAttribute("error", "El candidato no se encuentra en el sistema");					
					return "redirect:/candidatos/ver";
				}
			}else {
				redirect.addFlashAttribute("error", "El id del candidato no puede ser cero");
				return "redirect:/candidatos/ver";
			}
			
			model.addAttribute("candidato", candidato);
			
		}catch(Exception e) {
			
			redirect.addFlashAttribute("error", "Ocurrió algún error al momento de realizar la edición");
			e.printStackTrace();			
		}
		
		return "catalogos/candidatos/editar";
		
	}
	
	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardar(@Valid Candidato candidato, BindingResult result, RedirectAttributes redirect, SessionStatus status) {
		
		try {
			
			if(result.hasErrors()) {
				return "catalogos/candidatos/crear";
			}
			
			String resultado = (candidato.getId() != null) ? "El registro se ha editado con éxito" : "El registro se ha creado con éxito";
	
			candidato.setEstatus(3);
			
			candidatoService.save(candidato);
			status.setComplete();
			
			redirect.addFlashAttribute("success", resultado);
				
		}catch(Exception e) {
			
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un error al momento de guardar el candidato");
			return "redirect:/candidatos/ver";
		}
		
		return "redirect:/candidatos/ver";
	}
	
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable("id")Long id, RedirectAttributes redirect) {
		
		Candidato candidato = null;
		
		try {
			
			if(id > 0) {
				candidato = candidatoService.findById(id);
				if(candidato == null) {
					redirect.addFlashAttribute("error", "El candidato no existe en el sistema");
					
					return "redirect:/candidatos/ver";
				}else {
					candidatoService.deleteById(id);
				}
			}else {
				redirect.addFlashAttribute("error", "El id del Candidato no puede ser cero");
				
				return "redirect:/candidatos/ver";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Error al momento de borrar el registro");
			
			return "redirect:/candidatos/ver";
		}
		
		redirect.addFlashAttribute("success", "El registro se ha eliminado correctamente");
		return "redirect:/candidatos/ver";
	}
	
	/**
	 * Método para mostrar los periodos Dentro del list box de crear afiliados
	 * 
	 * @param(name ="periodos")
	 */

	@ModelAttribute("periodos")
	public List<Periodicidad> listaPeriodos() {
		return periodicidadService.findAll();
	}

	/**
	 * Método para mostrar los estados Dentro del list box de crear afiliados
	 * 
	 * @param(name = "estados")
	 */

	@ModelAttribute("estados")
	public List<String> getAllEstados() {
		return candidatoService.getAllEstados();
	}

	/**
	 * Método para mostrar los países Dentro del list box de crear afiliados
	 * 
	 * @param(name = "paises")
	 */

	@ModelAttribute("paises")
	public List<Paises> getAllPaises() {
		return candidatoService.getAllPaises();
	}

	/**
	 * Método para mostrar los servicios Dentro del list box de crear afiliados
	 * 
	 * @param(name = "servicios")
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
	public List<Promotor> getAllPromotores() {
		return promotorService.findAll();
	}

	/**
	 * Método para mostrar los servicios Dentro del list box de crear afiliados
	 * 
	 * @param(name = "cuentas")
	 */

	@ModelAttribute("cuentas")
	public List<Cuenta> getAllCuentas() {
		return cuentaService.findAll();
	}

	
}
