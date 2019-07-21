package com.prosesol.springboot.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.CentroContacto;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.service.IBeneficioService;
import com.prosesol.springboot.app.service.ICentroContactoService;
import com.prosesol.springboot.app.service.IRelServicioBeneficioService;
import com.prosesol.springboot.app.service.IServicioService;

@Controller
@SessionAttributes("servicio")
@RequestMapping("/servicios")
public class ServicioController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IServicioService servicioService;

	@Autowired
	private IBeneficioService beneficioService;

	@Autowired
	private ICentroContactoService centroContactoService;

	@Autowired
	private IRelServicioBeneficioService relServicioBeneficioService;

	/**
	 * Método para la creación de un Servicio
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Servicio servicio = new Servicio();

		model.put("servicio", servicio);
		model.put("titulo", "Crear Servicio");

		logger.info("Id servicio desde el método de crear: " + servicio.getId());

		return "catalogos/servicios/crear";

	}

	/**
	 * Método para la visualización del Servicio
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {

		model.addAttribute("titulo", "Membresía");
		model.addAttribute("servicios", servicioService.findAll());

		return "catalogos/servicios/ver";
	}

	/**
	 * Método para el detalle del Servicio
	 * 
	 * @param id
	 * @param model
	 * @param redirect
	 * @return
	 */

	@RequestMapping(value = "/detalle/{id}")
	public String detalle(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Servicio servicio = null;
		List<RelServicioBeneficio> listaServicioBeneficio = relServicioBeneficioService
				.getRelServicioBeneficioByIdServicio(id);

		List<Beneficio> beneficios = new ArrayList<Beneficio>();

		if (id > 0) {
			servicio = servicioService.findById(id);

			if (servicio == null) {
				redirect.addFlashAttribute("error", "El servicio no existe en la base de datos");
				return "redirect:/servicios/ver";
			}
		} else {
			redirect.addFlashAttribute("error", "El id del servicio no puede ser cero");
			return "redirect:/servicios/ver";
		}

		for (RelServicioBeneficio listabeneficio : listaServicioBeneficio) {
			Beneficio beneficio = new Beneficio();

			beneficio = beneficioService.findById(listabeneficio.getBeneficio().getId());
			beneficios.add(beneficio);
		}

		model.put("servicio", servicio);
		model.put("listaBeneficios", beneficios);

		return "catalogos/servicios/detalle";

	}

	/**
	 * Método para guardar el servicios en la BBDD
	 * 
	 * @param servicio
	 * @param result
	 * @param model
	 * @param redirect
	 * @param status
	 * @return
	 */

	@Secured("ROLE_ADMINISTRADOR")
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@RequestParam(name = "beneficio[]", required = false) Long[] idBeneficio,
			@RequestParam(name = "descripcion[]", required = false) String[] descripcion,
			@RequestParam(name = "costo[]", required = false) Double[] costo, @Valid Servicio servicio,
			BindingResult result, Model model, RedirectAttributes redirect, SessionStatus status) {

		logger.info("Entra al método para guardar o modificar el servicio");

		RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio();
		Long[] nBeneficio = Arrays.copyOfRange(idBeneficio, 1, idBeneficio.length);
		String[] nDescripcion = Arrays.copyOfRange(descripcion, 1, descripcion.length);
		Double[] nCosto = Arrays.copyOfRange(costo, 1, costo.length);
				
		try {
						
			if (servicio.getId() != null) {
				if (result.hasErrors()) {
					model.addAttribute("titulo", "Crear Membresia");
					return "catalogos/servicios/editar";
				}
			} else {
				if (result.hasErrors()) {
					model.addAttribute("titulo", "Crear Membresia");
					return "catalogos/servicios/crear";
				}
			}

			String flashMessage = (servicio.getId() != null) ? "Registro editado con éxito"
					: "Registro creado con éxito";

			servicio.setEstatus(true);			

			servicioService.save(servicio);
			
			for (int i = 0; i < nBeneficio.length; i++) {

				Beneficio beneficio = beneficioService.findById(nBeneficio[i]);

				relServicioBeneficio.setServicio(servicio);
				relServicioBeneficio.setBeneficio(beneficio);
				relServicioBeneficio.setDescripcion(nDescripcion[i]);
				relServicioBeneficio.setCosto(nCosto[i]);
				relServicioBeneficioService.save(relServicioBeneficio);

			}			
			
			status.setComplete();
			redirect.addFlashAttribute("success", flashMessage);
		} catch (Exception ex) {

			if (servicio.getId() != null) {
				redirect.addFlashAttribute("error", "Error al momento de guardar el servicio");
			} else {
				redirect.addFlashAttribute("error", "El servicio no se ha podido guardar");
				servicioService.delete(servicio.getId());
			}

			logger.error("Error al momento de guardar el servicio", ex);

			return "redirect:/servicios/ver";

		}

		logger.info("Id servicio desde el método de guardar: " + servicio.getId());

		return "redirect:/servicios/ver";

	}

	/**
	 * Método para la edición del Servicio
	 * 
	 * @param id
	 * @param model
	 * @param redirect
	 * @return
	 */

	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes redirect) {

		Servicio servicio = null;

		try {
			if (id > 0) {
				servicio = servicioService.findById(id);
				if (servicio == null) {
					redirect.addFlashAttribute("error", "El id del servicio no existe");
					return "redirect:/servicios/ver";
				}
			} else {
				redirect.addFlashAttribute("error", "El id del servicio no puede ser cero");
				return "redirect:/servicios/ver";
			}
		} catch (Exception ex) {
			redirect.addFlashAttribute("error", "Ocurrió un error en el sistema, contacte al administrador");
			return "redirect:/servicios/ver";
		}

		model.addAttribute("servicio", servicio);
		model.addAttribute("relServiciosBeneficios", getRelServicioBeneficioByIdServicio(id));

		return "catalogos/servicios/editar";

	}

	/**
	 * Método para borrar el Servicio
	 * 
	 * @param id
	 * @param redirect
	 * @return
	 */

	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {

		if (id > 0) {
			servicioService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}

		return "redirect:/servicios/ver";
	}

	/**
	 * Cátalogo de beneficios para la vista de creación de servicios
	 * 
	 * @return
	 */

	@ModelAttribute("lBeneficios")
	public List<Beneficio> getAllBeneficios() {
		return beneficioService.findAll();
	}

	/**
	 * Se genera un beneficio para la pantalla de creación del Servicio
	 * 
	 * @return
	 */

	@ModelAttribute("beneficio")
	public List<Beneficio> getBeneficioWrapper() {
		List<Beneficio> beneficio = new ArrayList<Beneficio>();

		return beneficio;
	}

	/**
	 * Método que obtiene la lista de los beneficios que pertenecen a cada Servicio
	 * 
	 * @param id
	 * @return
	 */

	@ModelAttribute("listaBeneficios")
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long idServicio) {

		List<RelServicioBeneficio> relServicioBeneficio = relServicioBeneficioService
				.getRelServicioBeneficioByIdServicio(idServicio);

		return relServicioBeneficio;
	}

	/**
	 * Método que obtiene la lista de los centros de asistencia por cada servicio
	 * 
	 * @return
	 */

	@ModelAttribute("centros")
	public List<CentroContacto> getAllCentroContacto() {

		List<CentroContacto> centrosContacto = centroContactoService.findAll();

		return centrosContacto;

	}

}
