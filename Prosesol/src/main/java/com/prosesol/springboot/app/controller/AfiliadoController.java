package com.prosesol.springboot.app.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IEmailService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.Mail;
import com.prosesol.springboot.app.util.Paises;
import com.prosesol.springboot.app.validator.ValidarMesesImpl;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/afiliados")
public class AfiliadoController {

	protected static final Log logger = LogFactory.getLog(AfiliadoController.class);

	private static String bandera = "inscripcion";

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
	private IEmailService emailService;

	@Autowired
	private ValidarMesesImpl validarMeses;

	@Autowired
	private CalcularFecha calcularFechas;

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
		model.put("beneficiarios", beneficiarios);
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
	public String guardar(@ModelAttribute(name = "clave") String clave, @Valid Afiliado afiliado, BindingResult result,
			Model model, RedirectAttributes redirect, SessionStatus status) {

		System.out.println(clave);

		Periodicidad periodicidad = new Periodicidad();
		Mail mail = new Mail();

		Map<String, Object> modelEmail = new HashMap<String, Object>();

		String mensajeFlash = null;

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

				// Calcular la fecha de corte por periodo
				periodicidad = periodicidadService.findById(afiliado.getPeriodicidad().getId());

				Date fechaCorte = calcularFechas.calcularFechas(periodicidad, afiliado.getCorte());

				afiliado.setFechaCorte(fechaCorte);
				afiliado.setFechaAlta(date);
				afiliado.setSaldoAcumulado(afiliado.getServicio().getCosto());
				afiliado.setClave(clave);

				// Se crea el cuerpo del mensaje para los afiliados con inscripción

				mail.setTo(afiliado.getEmail());
				mail.setFrom("prosesol@example.org");
				mail.setSubject("BIENVENIDO A PROSESOL");

				modelEmail.put("afiliado", afiliado);

				mail.setModel(modelEmail);

//				emailService.sendSimpleMessage(mail, bandera);

			}

			afiliado.setEstatus(3);
			logger.info(mensajeFlash);

			afiliadoService.save(afiliado);
			status.setComplete();

		} catch (Exception e) {

			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);
			return "/error/error_500";
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

	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/ver/{id}")
	public String actDesctAfiliado(@PathVariable(value = "id") Long id, RedirectAttributes redirect,
			SessionStatus status) {

		Afiliado afiliado = afiliadoService.findById(id);

		if (afiliado.getEstatus() == 3) {
			afiliado.setEstatus(1);
		} else if (afiliado.getEstatus() == 1) {
			afiliado.setEstatus(2);
		}

		afiliadoService.save(afiliado);
		status.setComplete();

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
		return afiliadoService.getAllEstados();
	}

	/**
	 * Método para mostrar los países Dentro del list box de crear afiliados
	 * 
	 * @param(name = "paises")
	 */

	@ModelAttribute("paises")
	public List<Paises> getAllPaises() {
		return afiliadoService.getAllPaises();
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

	/**
	 * Método para asignar una clave para el Afiliado
	 * 
	 * @param(name = "clave")
	 */

	@ModelAttribute("clave")
	public String getClaveAfiliado() {

		String clave = "0123456789";
		String claveAfiliado = "";

		for (int i = 0; i < 10; i++) {
			claveAfiliado += (clave.charAt((int) (Math.random() * clave.length())));
		}

		return claveAfiliado;
	}

}
