package com.prosesol.springboot.app.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.util.GenerarClave;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
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

import com.prosesol.springboot.app.util.Paises;

@Controller
@RequestMapping("candidatos")
@SessionAttributes(value = "candidato")
public class CandidatoController {

	private final static Log LOG = LogFactory.getLog(CandidatoController.class);

	@Value("${app.clave}")
	private String clave;

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

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private GenerarClave generarClave;
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@GetMapping(value = "/ver")
	public String ver(Model model) {
		
		model.addAttribute("candidatos", candidatoService.findAll());
		
		return "catalogos/candidatos/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@GetMapping(value = "/crear")
	public String crear(Model model) {
		
		Candidato candidato = new Candidato();
		model.addAttribute("candidato", candidato);
		
		return "catalogos/candidatos/crear";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
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
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardar(@Valid Candidato candidato, BindingResult result, RedirectAttributes redirect, SessionStatus status) {

		try {
			
			if(result.hasErrors()) {
				return "catalogos/candidatos/crear";
			}

			LocalDate fechaNacimiento = candidato.getFechaNacimiento().toInstant()
												 .atZone(ZoneId.systemDefault())
												 .toLocalDate();

			Rfc rfc = new Rfc.Builder()
							 .name(candidato.getNombre())
					  		 .firstLastName(candidato.getApellidoPaterno())
							 .secondLastName(candidato.getApellidoMaterno())
							 .birthday(fechaNacimiento.getDayOfMonth(), fechaNacimiento.getMonthValue(), fechaNacimiento.getYear())
							 .build();

			String resultado = (candidato.getId() != null) ? "El registro se ha editado con éxito" : "El registro se ha creado con éxito";
	
			candidato.setEstatus(3);
			candidato.setRfc(rfc.toString());
			
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
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
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
	 * Método que cambia el estatus del candidato de Candidato a Activo
	 * @param id
	 * @param redirect
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/estatus/{id}")
	public String activarCandidato(@PathVariable("id") Long id, RedirectAttributes redirect, SessionStatus status){

		Candidato candidato = candidatoService.findById(id);
		String flashMessage = "";
		try{

			candidato.setEstatus(1);
			candidato.setIsBeneficiario(false);
			candidato.setClave(generarClave.getClave(clave));
			candidato.setFechaAlta(new Date());
			candidato.setSaldoAcumulado(candidato.getServicio().getCostoTitular() + candidato.getServicio().getInscripcionTitular());
			candidato.setSaldoCorte(candidato.getServicio().getCostoTitular() + candidato.getServicio().getInscripcionTitular());

			candidatoService.insertCandidatoIntoAfiliado(candidato);
			candidatoService.deleteById(id);

			flashMessage = "El candidato se ha activo con número de clave: " + candidato.getClave();

			status.setComplete();
		}catch (Exception e){
			LOG.error("Error al momento de activar al candidato", e);
			redirect.addFlashAttribute("error", "Error a momento de activar al candidato");
			return "redirect:/candidatos/ver";
		}

		redirect.addFlashAttribute("success", flashMessage);

		return "redirect:/afiliados/ver";
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
