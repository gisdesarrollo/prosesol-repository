package com.prosesol.springboot.app.controller;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.services.EmailService;
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.GenerarClave;
import com.prosesol.springboot.app.util.Paises;
import com.prosesol.springboot.app.util.paginator.PageRender;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/afiliados")
public class AfiliadoController {

	protected static final Log logger = LogFactory.getLog(AfiliadoController.class);
	
	private final static int ID_TEMPLATE_BA =3469976;
	
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
	private IBeneficioService BeneficioService;

	@Autowired
	private CalcularFecha calcularFechas;

	@Autowired
	private ReportesExcelImpl reportesExcelImpl;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private GenerarClave generarClave;
	
	 @Autowired
	 private EmailService emailController;

	 
	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Afiliado afiliado = new Afiliado();
		model.put("afiliado", afiliado);
		return "catalogos/afiliados/crear";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
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

		return "catalogos/afiliados/detalle";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect,
			Locale locale) {

		logger.info("Editar afiliado: " + id);

		Afiliado afiliado = null;
		DateFormat formatoFecha = new SimpleDateFormat("dd");
		String dia;
		Integer diaCorte = 0;
		if (id > 0) {
			afiliado = afiliadoService.findById(id);
			if (afiliado == null) {
				redirect.addFlashAttribute("error",
						messageSource.getMessage("text.afiliado.ver.db.error", null, locale));
				return "redirect:/afiliados/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del afiliado no puede ser cero");
			return "redirect:/afiliados/ver";
		}
		if (afiliado.getFechaCorte() != null) {
			dia = formatoFecha.format(afiliado.getFechaCorte());
			diaCorte = Integer.parseInt(dia);
			model.put("diaCorte", diaCorte);

		} else {
			dia = null;
			model.put("diaCorte", dia);
		}
		model.put("afiliado", afiliado);

		return "catalogos/afiliados/editar";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result,
			@RequestParam(value = "fecha", required = false) boolean corte, Model model, RedirectAttributes redirect,
			SessionStatus status) {

		Periodicidad periodicidad = new Periodicidad();
		String mensajeFlash = null;
		Double saldoAcumulado;
		Date date = new Date();
		Rfc rfc = null;
		DateFormat formatoFecha = new SimpleDateFormat("dd");
		String dia;
		Integer diaCorte = 0;
		Date fechaCorte;
		DateFormat formatoMesYear;
		String fechaAfiliacion;
		String fechaHoy;
		Date mYA;
		Date mYH;
		String valida = "false";
		List<String> correos = new ArrayList<>();
		Map<String, String> modelo = new LinkedHashMap<>();
		JSONArray ABeneficioD = new JSONArray();
		try {

			if (result.hasErrors()) {

				return "catalogos/afiliados/crear";
			}
			if (afiliado.getId() != null) {

				if (afiliado.getIsBeneficiario().equals(true)) {
					afiliado.setIsBeneficiario(true);
				} else {
					afiliado.setIsBeneficiario(false);
				}
				if (afiliado.getFechaAfiliacion() == null) {
					model.addAttribute("error", "La fecha de afiliacion no debe quedar vació");
					return "catalogos/afiliados/crear";

				} else {
					formatoMesYear = new SimpleDateFormat("MM/yyyy");
					fechaAfiliacion = formatoMesYear.format(afiliado.getFechaAfiliacion());
					fechaHoy = formatoMesYear.format(new Date());
					mYA = formatoMesYear.parse(fechaAfiliacion);
					mYH = formatoMesYear.parse(fechaHoy);
					if (corte) {
						// evalua si esta dentro del mes o año actual
						if (mYH.equals(mYA)) {
							fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), afiliado.getCorte());
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
							
						}
						// evalua si esta fuera del mes o año actual
						if (mYH.after(mYA)) {
							fechaCorte = calcularFechas.calcularFechaAtrasada(afiliado.getFechaAfiliacion(),
									afiliado.getPeriodicidad(), afiliado.getCorte());
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
						}

					} else {
						dia = formatoFecha.format(afiliado.getFechaAfiliacion());
						diaCorte = Integer.parseInt(dia);
						// evalua si esta dentro del mes o año actual
						if (mYH.equals(mYA)) {
							fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), diaCorte);
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);

						}

						// evalua si esta fuera del mes o año actual
						if (mYH.after(mYA)) {
							fechaCorte = calcularFechas.calcularFechaAtrasada(afiliado.getFechaAfiliacion(),
									afiliado.getPeriodicidad(), diaCorte);
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
						}
					}
				}

				// Agrego costo de inscripción si se inscriben o descuento la inscripción
				if (afiliado.getIsIncripcion()) {
					saldoAcumulado = afiliado.getServicio().getCostoTitular() + afiliado.getServicio().getInscripcionTitular();
				} else {
					saldoAcumulado = afiliado.getServicio().getCostoTitular();
				}
				afiliado.setSaldoAcumulado(saldoAcumulado);

				mensajeFlash = "Registro editado con éxito";
			} else {

				if (afiliado.getRfc() == null || afiliado.getRfc().equals("")) {
					LocalDate fechaNacimiento = afiliado.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();

					rfc = new Rfc.Builder().name(afiliado.getNombre()).firstLastName(afiliado.getApellidoPaterno())
							.secondLastName(afiliado.getApellidoMaterno()).birthday(fechaNacimiento.getDayOfMonth(),
									fechaNacimiento.getMonthValue(), fechaNacimiento.getYear())
							.build();

					afiliado.setRfc(rfc.toString());

				}

				afiliado.setIsBeneficiario(false);

				// Calcular la fecha de corte por periodo
				periodicidad = periodicidadService.findById(afiliado.getPeriodicidad().getId());
				if (afiliado.getFechaAfiliacion() == null) {
					model.addAttribute("error", "La fecha de afiliacion no debe quedar vació");
					return "catalogos/afiliados/crear";
				} else {
					formatoMesYear = new SimpleDateFormat("MM/yyyy");
					fechaAfiliacion = formatoMesYear.format(afiliado.getFechaAfiliacion());
					fechaHoy = formatoMesYear.format(new Date());
					mYA = formatoMesYear.parse(fechaAfiliacion);
					mYH = formatoMesYear.parse(fechaHoy);
					if (corte) {
						// evalua si esta dentro del mes o año actual
						if (mYH.equals(mYA)) {
							fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), afiliado.getCorte());
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
							
						}
						// evalua si esta fuera del mes o año actual
						if (mYH.after(mYA)) {
							fechaCorte = calcularFechas.calcularFechaAtrasada(afiliado.getFechaAfiliacion(),
									afiliado.getPeriodicidad(), afiliado.getCorte());
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
						}

					} else {
						dia = formatoFecha.format(afiliado.getFechaAfiliacion());
						diaCorte = Integer.parseInt(dia);
						// evalua si esta dentro del mes o año actual
						if (mYH.equals(mYA)) {
							fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), diaCorte);
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);

							
						}
						// evalua si esta fuera del mes o año actual
						if (mYH.after(mYA)) {
							fechaCorte = calcularFechas.calcularFechaAtrasada(afiliado.getFechaAfiliacion(),
									afiliado.getPeriodicidad(), diaCorte);
							afiliado.setFechaCorte(fechaCorte);
							afiliado.setSaldoCorte(0.0);
						}
					}

				}
				// Agrego costo de inscripción si se inscriben
				if (afiliado.getIsIncripcion()) {
					saldoAcumulado = afiliado.getServicio().getCostoTitular()
							+ afiliado.getServicio().getInscripcionTitular();
				} else {
					saldoAcumulado = afiliado.getServicio().getCostoTitular();
				}

				afiliado.setSaldoAcumulado(saldoAcumulado);
				afiliado.setFechaAlta(date);

				afiliado.setClave(generarClave.getClave(clave));
				afiliado.setEstatus(2);
				mensajeFlash = "Registro creado con éxito";
				
				// Envío email bienvenida
				if (afiliado.getEmail()!=null) {
					
                   modelo.put("afiliado", afiliado.getNombre() + " " + afiliado.getApellidoPaterno() +
                           " " + afiliado.getApellidoMaterno());
                   modelo.put("servicio", afiliado.getServicio().getNombre());
                   modelo.put("rfc", afiliado.getRfc());
                   modelo.put("proveedor", afiliado.getServicio().getNombreProveedor());
                   modelo.put("telefono", afiliado.getServicio().getTelefono());
                   modelo.put("correo", afiliado.getServicio().getCorreo());
                   modelo.put("nota", afiliado.getServicio().getNota());
                   modelo.put("id",afiliado.getClave());
                   //modelo.put("contratante", "desc");
                  // modelo.put("valida",valida);
                   
                   correos.add(afiliado.getEmail());
                   List<Beneficio> relServcioBeneficio = BeneficioService.getBeneficiosByIdServicio(afiliado.getServicio().getId());
                   		for(Beneficio bene : relServcioBeneficio) {
                   			ABeneficioD.put(getBeneficios(bene.getNombre(), bene.getDescripcion()));
                   		}                                                          
				}
			}
			 
			logger.info(mensajeFlash);
			afiliadoService.save(afiliado);
			 logger.info("Enviando email de bienvenido afiliado..."); 
			 emailController.sendMailJet(modelo,ID_TEMPLATE_BA,correos,ABeneficioD,valida);
			 status.setComplete();
            	 
			

		} catch (DataIntegrityViolationException e) {

			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);

			redirect.addFlashAttribute("error", "El RFC ya existe en la base de datos: " + rfc);

			return "redirect:/afiliados/ver";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error al momento de ejecutar el proceso: " + e);

			redirect.addFlashAttribute("error", "Ocurrió un error al momento de insertar el Afiliado");

			return "redirect:/afiliados/ver";
		}

		redirect.addFlashAttribute("success", mensajeFlash);
		return "redirect:/afiliados/ver";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Afiliado> afiliados = afiliadoService.findAll(pageRequest);

		PageRender<Afiliado> pageRender = new PageRender<>("/afiliados/ver", afiliados);

		model.addAttribute("afiliados", afiliados);
		model.addAttribute("page", pageRender);

		return "catalogos/afiliados/ver";
	}
	
	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/activos", method = RequestMethod.GET)
	public String verActivos(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		return "catalogos/afiliados/verActivos";
	}
	
	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/vencidos", method = RequestMethod.GET)
	public String verVencidos(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		return "catalogos/afiliados/verVencidos";
	}
	
	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
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
	 * Método que cambia el estatus del afiliado 1.- Activo 2.- Inactivo 3.-
	 * Candidato
	 * 
	 * @param id
	 * @param status
	 * @return
	 */

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/cambiar_estatus/{id}")
	public String actDesctAfiliado(@PathVariable(value = "id") Long id,
			SessionStatus status) {

		Afiliado afiliado = afiliadoService.findById(id);

		if (afiliado.getEstatus() == 3) {
			afiliado.setEstatus(1);
		} else if (afiliado.getEstatus() == 1) {
			afiliado.setEstatus(2);
		} else if (afiliado.getEstatus() == 2) {
			afiliado.setEstatus(1);
		}
		if(afiliado.getEmail()==null || afiliado.getEmail().equals("")) {
			afiliado.setEmail("email_ficticio@gmail.com");
		}
		if(afiliado.getTelefonoMovil()==null || afiliado.getTelefonoMovil()==0) {
			afiliado.setTelefonoMovil(5555555555l);
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
	
	public JSONObject getBeneficios(String name, String descripcion) {
		
		JSONObject OBeneficioD = new JSONObject();
			OBeneficioD.put("nombre",name);
			OBeneficioD.put("descripcion",descripcion);
		   return OBeneficioD ;
	}

}
