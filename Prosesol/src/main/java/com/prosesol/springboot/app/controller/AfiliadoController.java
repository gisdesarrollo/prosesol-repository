package com.prosesol.springboot.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.Paises;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/afiliados")
public class AfiliadoController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IPeriodicidadService periodicidadService;

	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Afiliado afiliado = new Afiliado();

		model.put("afiliado", afiliado);
		model.put("titulo", "Crear Afiliado");

		return "catalogos/afiliados/crear";
	}

	@GetMapping(value = "/detalle/{id}")
	public String detalle(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Afiliado afiliado = afiliadoService.findById(id);
		List<Afiliado> beneficiarios = afiliadoService.getBeneficiarioByIdByIsBeneficiario(id);

		if (afiliado == null) {
			redirect.addFlashAttribute("error", "El id del afiliado no existe");
			return "redirect:/afiliados/ver";
		}

		model.put("afiliado", afiliadoService.findById(id));
		model.put("afiliados", beneficiarios);
		model.put("titulo", "Detalle Afiliado" + ' ' + afiliado.getNombre());

		return "catalogos/afiliados/detalle";

	}

	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		logger.info("Editar afiliado: " + id);

		Afiliado afiliado = null;

		if (id > 0) {
			afiliado = afiliadoService.findById(id);
			if (afiliado == null) {
				redirect.addFlashAttribute("Error: ", "El id del afiliado no existe");
				return "redirect:/afiliados/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del afiliado no puede ser cero");
			return "redirect:/afiliados/ver";
		}

		model.put("afiliado", afiliado);
		model.put("titulo", "Crear Afiliado");

		return "catalogos/afiliados/editar";

	}

	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model, RedirectAttributes redirect,
			SessionStatus status) {

		Periodicidad periodicidad = new Periodicidad();

		String mensajeFlash = null;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		Date date = new Date();

		try {

			if (result.hasErrors()) {
				model.addAttribute("titulo", "Crear Afiliado");
				return "catalogos/afiliados/crear";
			}

			if (afiliado.getId() != null) {
				if (afiliado.getIsBeneficiario().equals(true)) {
					afiliado.setIsBeneficiario(true);
				} else {
					afiliado.setIsBeneficiario(false);
				}
				mensajeFlash = "Registro editado con éxito";
			} else {
				afiliado.setIsBeneficiario(false);
				mensajeFlash = "Registro creado con éxito";
				dateFormat.format(date);

				// Calcular la fecha de corte por periodo
				periodicidad = periodicidadService.findById(afiliado.getPeriodicidad().getId());
				Date fechaCorte = calcularFechaCorte(periodicidad);

				afiliado.setFechaAlta(date);
				afiliado.setFechaCorte(fechaCorte);
			}

			afiliado.setEstatus(true);
			logger.info(mensajeFlash);

			afiliadoService.save(afiliado);
			status.setComplete();

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);
			return "/errores/error_403";
		}

		return "redirect:/afiliados/ver";
	}

	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model, Authentication authentication) {

		if (authentication != null) {
			logger.info("Usuario autenticado: ".concat(authentication.getName()));
		}

		model.addAttribute("titulo", "Afiliados");
		model.addAttribute("afiliados", afiliadoService.findAll());

		return "catalogos/afiliados/ver";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {

		logger.info("Entra el método de eliminación de afiliado");

		if (id > 0) {
			afiliadoService.delete(id);

			logger.info("El registro con el id: " + id + " se eliminará");
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}

		return "redirect:/afiliados/ver";
	}

	private static Date calcularFechaCorte(Periodicidad periodo) {

		int periodoTiempo;
		@SuppressWarnings("unused")
		int corte;

		LocalDate tiempoActual = LocalDate.now();
		LocalDate tiempoModificado = null;

		switch (periodo.getNombre()) {
		case "MENSUAL":

			periodoTiempo = 1;
			corte = periodo.getCorte();
			
			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);

			break;
		case "BIMESTRAL":
			
			periodoTiempo = 2;
			corte = periodo.getCorte();
			
			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);
			
			break;
		case "TRIMESTRAL":
			
			periodoTiempo = 3;
			corte = periodo.getCorte();

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);
			
			break;
		case "CUATRIMESTRAL":
			
			periodoTiempo = 4;
			corte = periodo.getCorte();

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);
			
			break;
		case "SEMESTRAL":
			
			periodoTiempo = 6;
			corte = periodo.getCorte();

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);
			
			break;
		case "ANUAL":
			
			periodoTiempo = 12;
			corte = periodo.getCorte();

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			
			System.out.println(tiempoModificado);
			
			break;

		default:
			new Exception();

		}

		return java.util.Date.from(tiempoModificado.atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());

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

}
