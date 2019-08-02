package com.prosesol.springboot.app.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
//import com.prosesol.springboot.app.service.IEmailService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.Paises;
import com.prosesol.springboot.app.util.paginator.PageRender;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/afiliados")
public class AfiliadoController {

	protected static final Log logger = LogFactory.getLog(AfiliadoController.class);
	
	@Value("${app.clave}")
	private String clave;

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

	@Autowired
	private ReportesExcelImpl reportesExcelImpl;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Afiliado afiliado = new Afiliado();
		
		model.put("afiliado", afiliado);
		
		return "catalogos/afiliados/crear";
	}

	@GetMapping(value = "/detalle/{id}")
	public String detalle(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect,
			Locale locale) {

		Afiliado afiliado = afiliadoService.findById(id);
		List<Afiliado> beneficiarios = afiliadoService.getBeneficiarioByIdByIsBeneficiario(id);

		if (afiliado == null) {
			redirect.addFlashAttribute("error", messageSource.getMessage("text.afiliado.ver.db.error", null, locale));
			return "redirect:/afiliados/ver";
		}

		model.put("afiliado", afiliadoService.findById(id));
		model.put("beneficiarios", beneficiarios);
		model.put("titulo", "Detalle Afiliado" + ' ' + afiliado.getNombre());

		return "catalogos/afiliados/detalle";

	}

	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect,
						Locale locale) {

		logger.info("Editar afiliado: " + id);

		Afiliado afiliado = null;

		if (id > 0) {
			afiliado = afiliadoService.findById(id);
			if (afiliado == null) {
				redirect.addFlashAttribute("error", messageSource.getMessage("text.afiliado.ver.db.error", null, locale));
				return "redirect:/afiliados/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del afiliado no puede ser cero");
			return "redirect:/afiliados/ver";
		}

		model.put("afiliado", afiliado);

		return "catalogos/afiliados/editar";

	}

	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@ModelAttribute(name = "clave") String clave, @Valid Afiliado afiliado, BindingResult result,
			Model model, RedirectAttributes redirect, SessionStatus status) {

		System.out.println(clave);
		Periodicidad periodicidad = new Periodicidad();
		String mensajeFlash = null;
		Date date = new Date();
		Rfc rfc = null;

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

				if(afiliado.getRfc() == null || afiliado.getRfc().equals("")) {
					LocalDate fechaNacimiento = afiliado.getFechaNacimiento().toInstant()
												.atZone(ZoneId.systemDefault())
												.toLocalDate();
					
					rfc = new Rfc.Builder()
								     .name(afiliado.getNombre())
								     .firstLastName(afiliado.getApellidoPaterno())
								     .secondLastName(afiliado.getApellidoMaterno())
								     .birthday(fechaNacimiento.getDayOfMonth(), fechaNacimiento.getMonthValue(), fechaNacimiento.getYear())
								     .build();
					
					afiliado.setRfc(rfc.toString());
					
					System.out.println(rfc.toString());
				}
				
				afiliado.setIsBeneficiario(false);			

				// Calcular la fecha de corte por periodo
				periodicidad = periodicidadService.findById(afiliado.getPeriodicidad().getId());
				Date fechaCorte = calcularFechas.calcularFechas(periodicidad, afiliado.getCorte());

				afiliado.setFechaCorte(fechaCorte);
				afiliado.setFechaAlta(date);
//				afiliado.setSaldoAcumulado(afiliado.getServicio().getCosto());
				afiliado.setClave(clave);
				
				mensajeFlash = "Registro creado con éxito";

			}

			afiliado.setEstatus(3);
			logger.info(mensajeFlash);

			afiliadoService.save(afiliado);
			status.setComplete();

		} catch (DataIntegrityViolationException e) {

			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);
			
			redirect.addFlashAttribute("error", "El RFC ya existe en la base de datos: " + rfc);
			
			return "redirect:/afiliados/ver";
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);
			
			redirect.addFlashAttribute("error", "Ocurrió un error al momento de insertar el Afiliado");
			
			return "redirect:/afiliados/ver";
		}

		return "redirect:/afiliados/ver";
	}

	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(@RequestParam(name="page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);
	
		Page<Afiliado> afiliados = afiliadoService.findAll(pageRequest);
		
		PageRender<Afiliado> pageRender = new PageRender<>("/afiliados/ver", afiliados);
		
		model.addAttribute("titulo", "Afiliados");
		model.addAttribute("afiliados", afiliados);
		model.addAttribute("page", pageRender);
		
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

	/**
	 * Método que cambia el estatus del afiliado
	 * 1.- Activo
	 * 2.- Inactivo
	 * 3.- Candidato
	 * @param id
	 * @param redirect
	 * @param status
	 * @return
	 */
	
	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/ver/{id}")
	public String actDesctAfiliado(@PathVariable(value = "id") Long id, RedirectAttributes redirect,
			SessionStatus status) {

		Afiliado afiliado = afiliadoService.findById(id);

		if (afiliado.getEstatus() == 3) {
			afiliado.setEstatus(1);
		} else if (afiliado.getEstatus() == 1) {
			afiliado.setEstatus(2);
		}else if (afiliado.getEstatus() == 2) {
			afiliado.setEstatus(1);
		}

		afiliadoService.save(afiliado);
		status.setComplete();

		return "redirect:/afiliados/ver";

	}
	
	/**
	 * Descargar Excel
	 */
	
	@Secured("ROLE_ADMINISTRADOR")
	@GetMapping("/descargar")
	public void descargar(HttpServletResponse response) throws Exception {

		List<Afiliado> afiliados = afiliadoService.findAll();
		reportesExcelImpl.generarReporteAfiliadoXlsx(afiliados, response);
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

		String claveAfiliado = "PR-";

		for (int i = 0; i < 10; i++) {
			claveAfiliado += (clave.charAt((int) (Math.random() * clave.length())));
		}

		return claveAfiliado;
	}

}
