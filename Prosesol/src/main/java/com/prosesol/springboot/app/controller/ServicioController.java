     package com.prosesol.springboot.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

	private Long idServicioGeneral;

	/**
	 * Método para la creación de un Servicio
	 * 
	 * @param model
	 * @return
	 */

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Servicio servicio = new Servicio();
		boolean titular = false;
		boolean beneficiario = false;

		model.put("servicio", servicio);
		model.put("titulo", "Crear Servicio");
		model.put("titular", titular);
		model.put("beneficiario", beneficiario);

		logger.info("Id servicio desde el método de crear: " + servicio.getId());

		return "catalogos/servicios/crear";

	}

	/**
	 * Método para la visualización del Servicio
	 * 
	 * @param model
	 * @return
	 */

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
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

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
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

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST, params = "action=save")
	public String guardar(@Valid Servicio servicio, BindingResult result, Model model, RedirectAttributes redirect,
			SessionStatus status, @RequestParam(name = "beneficio[]", required = false) List<Long> idBeneficio,
			@RequestParam(name = "descripcion[]", required = false) List<String> descripcion,
			@RequestParam(name = "beneficiario[]", required = false) List<Long> beneficiario,
			@RequestParam(name = "titular[]", required = false) List<Long> titular) throws Exception {

		logger.info("Entra al método para guardar o modificar el servicio");

		String flashMessage = "";
		RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio();

		if (result.hasErrors()) {
			redirect.addFlashAttribute("error", "Campos incompletos");
			return "catalogos/servicios/crear";
		}

		try {

			// Verifica si se necesita editar el servicio o se deberá de crear

			if (servicio.getId() != null) {
				
				servicio.setEstatus(true);
				servicioService.save(servicio);

				// Verifica si el servicio se editará con todo y beneficios

				if (idBeneficio != null && idBeneficio.size() > 0) {
					editarServiciosConBeneficios(servicio, idBeneficio, descripcion, titular, beneficiario);				
				}
				
				flashMessage = "Servicio editado correctamente";

			} else {

				// Verifica si el servicio se creará con todo y beneficios

				if (idBeneficio != null && idBeneficio.size() > 0) {

					descripcion.removeAll(Arrays.asList("", null));

					servicio.setEstatus(true);
					servicioService.save(servicio);

					int dIndex = 0;
					int tIndex = 0;
					int bIndex = 0;
					int countTitular = 1;
					int countBeneficiario = 1;

					for (Long beneficio : idBeneficio) {

						Beneficio nBeneficio = beneficioService.findById(beneficio);

						relServicioBeneficio.setServicio(servicio);
						relServicioBeneficio.setBeneficio(nBeneficio);
						relServicioBeneficio.setDescripcion(descripcion.get(dIndex));

						if (titular != null && titular.size() >= countTitular) {
							if (beneficio == titular.get(tIndex)) {
								relServicioBeneficio.setTitular(true);
								relServicioBeneficio.setBeneficiario(false);
								tIndex++;
								countTitular++;
							}
						}

						if (beneficiario != null && beneficiario.size() >= countBeneficiario) {
							if (beneficio == beneficiario.get(bIndex)) {
								relServicioBeneficio.setTitular(false);
								relServicioBeneficio.setBeneficiario(true);
								bIndex++;
								countBeneficiario++;
							}
						}

						relServicioBeneficioService.save(relServicioBeneficio);
						dIndex++;
					}
					
					flashMessage = "Servicio creado correctamente";

				} else { // Solamente se inserta el servicio

					servicio.setEstatus(true);
					servicioService.save(servicio);
					
					flashMessage = "Servicio creado correctamente";
				}

			}	
			
			status.setComplete();
			redirect.addFlashAttribute("success", flashMessage);

		} catch (Exception ex) {
			ex.printStackTrace();
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

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long idServicio, Model model, RedirectAttributes redirect) {

		Servicio servicio = null;
		List<RelServicioBeneficio> relServicioBeneficios = getRelServicioBeneficioByIdServicio(idServicio);
		List<Beneficio> beneficios = beneficioService.findAll();
		List<RelServicioBeneficio> nRelServicioBeneficio = new ArrayList<RelServicioBeneficio>();

		try {
			if (idServicio > 0) {
				servicio = servicioService.findById(idServicio);
				if (servicio == null) {
					redirect.addFlashAttribute("error", "El id del servicio no existe");
					return "redirect:/servicios/ver";
				}

				int countSB = 0;

				for (Beneficio beneficio : beneficios) {
					if (relServicioBeneficios.size() > countSB
							&& beneficio.getId() == relServicioBeneficios.get(countSB).getBeneficio().getId()) {

						RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio(
								relServicioBeneficios.get(countSB).getServicio(),
								relServicioBeneficios.get(countSB).getBeneficio(),
								relServicioBeneficios.get(countSB).getTitular(),
								relServicioBeneficios.get(countSB).getBeneficiario(),
								relServicioBeneficios.get(countSB).getDescripcion());

						nRelServicioBeneficio.add(relServicioBeneficio);

						countSB++;
					} else {

						RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio, false,
								false, null);

						nRelServicioBeneficio.add(relServicioBeneficio);
					}

				}

			} else {
				redirect.addFlashAttribute("error", "El id del servicio no puede ser cero");
				return "redirect:/servicios/ver";
			}
		} catch (Exception ex) {
			redirect.addFlashAttribute("error", "Ocurrió un error en el sistema, contacte al administrador");
			ex.printStackTrace();
			return "redirect:/servicios/ver";
		}

		idServicioGeneral = idServicio;

		model.addAttribute("servicio", servicio);
		model.addAttribute("relServicioBeneficios", nRelServicioBeneficio);

		return "catalogos/servicios/editar";

	}

	/**
	 * Método para borrar el Servicio
	 * 
	 * @param id
	 * @param redirect
	 * @return
	 */

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {

		try {
			if (id > 0) {
				servicioService.delete(id);
				redirect.addFlashAttribute("success", "Registro eliminado correctamente");
			}
		} catch (Exception e) {
			logger.error("Ocurrió un error al momento de eliminar el registro", e);
			redirect.addFlashAttribute("error", "El servicio no se puede eliminar porque está asignado a un Afiliado");
		}

		return "redirect:/servicios/ver";
	}

	/**
	 * Método para borrar los beneficios en una lista
	 * 
	 * @param beneficios
	 * @param model
	 * @param redirect
	 * @return
	 */

	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST, params = "action=delete")
	public String borrarBeneficios(@RequestParam(name = "beneficio[]", required = false) List<Long> beneficios,
			Model model, RedirectAttributes redirect) {
		try {

			for (Long beneficio : beneficios) {
				relServicioBeneficioService.removeBeneficiobyIdBeneficio(beneficio);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Ocurrió un error al momento de realizar la eliminación de los beneficios", e);
			return "redirect:/servicios/ver";
		}

		redirect.addFlashAttribute("success", "Beneficios eliminados correctamente");

		return "redirect:/servicios/editar/" + idServicioGeneral;
	}

	/**
	 * Cátalogo de beneficios para la vista de creación de servicios
	 * 
	 * @return
	 */

	@ModelAttribute("beneficios")
	public List<Beneficio> getAllBeneficios() {
		return beneficioService.findAll();
	}

	/**
	 * Método que obtiene la lista de los beneficios que pertenecen a cada Servicio
	 * 
	 * @param id
	 * @return
	 */

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

	/**
	 * Método que edita o inserta beneficios
	 * 
	 * @param servicio
	 * @param idBeneficio
	 * @param descripcion
	 * @param titular'
	 * @param beneficiario
	 */

	public void editarServiciosConBeneficios(Servicio servicio, List<Long> idBeneficio, List<String> descripcion,
			List<Long> titular, List<Long> beneficiario) {

		RelServicioBeneficio relServicioBeneficio = null;
		List<Beneficio> beneficios = beneficioService.findAll();
		Map<Long, String> beneficioDescripcion = new HashMap<Long, String>();
		
		int countDescripcion = 0;
		
		for(Beneficio b : beneficios) {
			beneficioDescripcion.put(b.getId(), descripcion.get(countDescripcion));
			countDescripcion++;
		}
		
		for(Long id : idBeneficio) {
			 Beneficio beneficio = beneficioService.findById(id);
			 
			 boolean isTitular = false;
			 boolean isBeneficiario = false;
			 
			 for(Long idTitular : titular) {				 
				 if(titular != null && id == idTitular) {					 
					 for(Map.Entry<Long, String> entry : beneficioDescripcion.entrySet()) {
							if(entry.getKey() == id) {
								relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio, true, false, entry.getValue());
								break;
							}
					 }			
					 
					 isTitular = true;
					 break;
				 }
			 }
			 
			 for(Long idBeneficiario : beneficiario) {
				 if(beneficiario != null && id == idBeneficiario) {
					 
					 for(Map.Entry<Long, String> entry : beneficioDescripcion.entrySet()) {
							if(entry.getKey() == id) {
								relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio, false, true, entry.getValue());
								break;
							}
					 }
					 
					 isBeneficiario = true;	
					 break;
				 }			 
			 }
			 if(isTitular && isBeneficiario) {
				 for(Map.Entry<Long, String> entry : beneficioDescripcion.entrySet()) {
						if(entry.getKey() == id) {
							relServicioBeneficio = new RelServicioBeneficio(servicio, beneficio, true, true, entry.getValue());
							break;
						}
				 }			 
			 }
			 
			 relServicioBeneficioService.save(relServicioBeneficio);
		}
	}

}
