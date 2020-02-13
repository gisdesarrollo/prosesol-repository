package com.prosesol.springboot.app.controller;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.GenerarClave;
import com.prosesol.springboot.app.util.Paises;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/beneficiarios")
public class BeneficiarioController {

	protected Log logger = LogFactory.getLog(BeneficiarioController.class);

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
	private GenerarClave generarClave;
	
	private static Long idAfiliado;
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear/{id}")
	public String crear(@PathVariable("id")Long id, Map<String, Object> model) {
		
		idAfiliado = id;
		System.out.println(idAfiliado);
		
		Afiliado beneficiario = new Afiliado();
		
		model.put("afiliado", beneficiario);
		model.put("titulo", "Crear Beneficiario");

		return "catalogos/beneficiarios/crear";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(Afiliado afiliado, BindingResult result, Model model,
						  RedirectAttributes redirect, SessionStatus status) {

		Rfc rfc;
		
		Date fechaAlta = new Date();
		try {

			Afiliado titular = afiliadoService.findById(idAfiliado);
			Double saldoAcumuladoTitular = titular.getSaldoAcumulado();

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

			}

			afiliado.setEstatus(1);
			afiliado.setIsBeneficiario(true);
			afiliado.setClave(generarClave.getClave(clave));
			afiliado.setFechaAlta(fechaAlta);
			afiliado.setServicio(titular.getServicio());
			afiliado.setFechaCorte(titular.getFechaCorte());
			afiliado.setCuenta(titular.getCuenta());
			afiliado.setPeriodicidad(titular.getPeriodicidad());

			Double saldoAcumuladoBeneficiario = titular.getServicio().getInscripcionBeneficiario() + titular.getServicio().
					getCostoBeneficiario();

			saldoAcumuladoTitular = saldoAcumuladoTitular + saldoAcumuladoBeneficiario;
			
			afiliadoService.save(afiliado);
			guardarRelAfiliadoBeneficiario(afiliado, idAfiliado);

			titular.setSaldoAcumulado(saldoAcumuladoTitular);
			afiliadoService.save(titular);
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
}
