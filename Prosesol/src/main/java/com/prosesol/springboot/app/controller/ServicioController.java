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
import com.prosesol.springboot.app.entity.dto.RelServicioBeneficioDto;
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
	public String guardar(@Valid Servicio servicio,  BindingResult result, Model model,
			RedirectAttributes redirect, SessionStatus status,
			@RequestParam(name = "beneficio[]", required = false) List<Long> idBeneficio,
			@RequestParam(name = "descripcion[]", required = false) List<String> descripcion,
			@RequestParam(name = "beneficiario[]", required = false) List<Long> beneficiario,
			@RequestParam(name = "titular[]", required = false) List<Long> titular,
			@ModelAttribute RelServicioBeneficioDto relServicioBeneficios) throws Exception {

		logger.info("Entra al método para guardar o modificar el servicio");

		String flashMessage = "";
		RelServicioBeneficio relServicioBeneficio = new RelServicioBeneficio();
		
		if (result.hasErrors()) {
			redirect.addFlashAttribute("error", "Campos incompletos");
			return "catalogos/servicios/crear";
		}
		
		try {
			
			flashMessage = (servicio.getId() != null) ? "Registro editado correctamente"
					: "Registro creado correctamente";

			if (servicio.getId() != null) {

				servicio.setEstatus(true);
				servicioService.save(servicio);
				
				int counter = 0;

//				for (RelServicioBeneficio relSB : relServicioBeneficios.getRelServicioBeneficios()) {
//
//					if (relSB.getBeneficio() != null) {
//
//						System.out.println(relSB.getBeneficio().getId());
//						System.out.println(idBeneficio[counter]);
//						
//						if(relSB.getBeneficio().getId() != idBeneficio[counter]) {
//							relServicioBeneficioService.deleteBeneficioByIdBeneficioAndIdServicio(servicio.getId(), relSB.getBeneficio().getId());
//						}
//
//						Beneficio beneficio = beneficioService.findById(idBeneficio[counter]);
//
//						relServicioBeneficio.setServicio(servicio);
//						relServicioBeneficio.setBeneficio(beneficio);
//						relServicioBeneficio.setDescripcion(relSB.getDescripcion());
//						relServicioBeneficioService.save(relServicioBeneficio);
//
//						counter++;
//
//					} 
//				}

			} else {

				if(idBeneficio.size() > 0) {
					
					descripcion.removeAll(Arrays.asList("", null));
					
					servicio.setEstatus(true);
					servicioService.save(servicio);
					
					int dIndex = 0;
					int tIndex = 0;
					int bIndex = 0;
					int countTitular = 1;
					int countBeneficiario = 1;
					
					for(Long beneficio : idBeneficio) {
						
						Beneficio nBeneficio = beneficioService.findById(beneficio);
						
						relServicioBeneficio.setServicio(servicio);
						relServicioBeneficio.setBeneficio(nBeneficio);
						relServicioBeneficio.setDescripcion(descripcion.get(dIndex));
						
						if(titular != null && titular.size() >= countTitular) {
							if(beneficio == titular.get(tIndex)) {
								relServicioBeneficio.setTitular(true);
								relServicioBeneficio.setBeneficiario(false);
								tIndex++;
								countTitular++;
							}							
						}
						
						if(beneficiario != null && beneficiario.size() >= countBeneficiario) {
							if(beneficio == beneficiario.get(bIndex)) {
								relServicioBeneficio.setTitular(false);
								relServicioBeneficio.setBeneficiario(true);
								bIndex++;
								countBeneficiario++;
							}							
						}				
						
						relServicioBeneficioService.save(relServicioBeneficio);						
						dIndex++;
					}
					
				}else {
					servicio.setEstatus(true);
					servicioService.save(servicio);
				}
				
			}

			status.setComplete();
			redirect.addFlashAttribute("success", flashMessage);

		} catch (Exception ex) {

//			if (servicio.getId() != null) {
//				redirect.addFlashAttribute("error", "Error al momento de guardar el servicio");
//				ex.printStackTrace();
//				return "redirect:/servicios/ver";
//			} else {
//				redirect.addFlashAttribute("error", "El servicio no se ha podido guardar");
//				servicioService.delete(servicio.getId());
//				return "redirect:/servicios/ver";
//			}
			
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
		model.addAttribute("relServicioBeneficios", getRelServicioBeneficioByIdServicio(id));

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

	@RequestMapping(value = "/eliminarBeneficios/{id}")
	public String borrarBeneficios(@PathVariable(value = "id")Long id, @RequestParam("beneficios[]") Long[] beneficios[],
								   Model model, RedirectAttributes redirect) {
		
		Servicio servicio = servicioService.findById(id);
		long idBeneficio = 0;
		
		for(int i = 0; i < beneficios.length; i++) {
			
			
			
//			relServicioBeneficioService.deleteBeneficioByIdBeneficioAndIdServicio(id, idBeneficio);
		}
		
		return "";
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

}
