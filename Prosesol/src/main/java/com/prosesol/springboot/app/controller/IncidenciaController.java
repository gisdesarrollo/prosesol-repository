package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.entity.custom.IncidenciaCustom;
import com.prosesol.springboot.app.entity.custom.RelAfiliadoIncidenciaBeneficioCustom;
import com.prosesol.springboot.app.entity.dto.RelServicioBeneficioDto;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/incidencias")
@SessionAttributes("incidencia")
public class IncidenciaController {

	protected static final Log LOG = LogFactory.getLog(IncidenciaController.class);

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private IIncidenciaService incidenciaService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IRelServicioBeneficioService relServicioBeneficio;

	@Autowired
	private IRelAfiliadoIncidenciaBeneficioService relAfiliadoIncidenciaBeneficioService;

	@Autowired
	private IRelAfiliadoIncidenciaService relAfiliadoIncidenciaService;

	@Autowired
	private IBeneficioService beneficioService;

	private Long idAfiliado;
	
	@Secured("ROLE_ASISTENCIA")
	@GetMapping(value = "/home")
	public String home() {
		return "incidencias/home";
	}

	/**
	 * Método que muestra todas las incidencias creadas en la pantalla de home
	 * 
	 * @param model
	 * @return
	 */

	@Secured("ROLE_ASISTENCIA")
	@GetMapping(value = "/ver")
	public String ver(Model model, RedirectAttributes redirect) {

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Usuario usuario = usuarioService.findByUsername(username);

			List<Incidencia> incidencias = incidenciaService.getIncidenciasByUserId(usuario.getId());

			model.addAttribute("incidencias", incidencias);

		} catch (Exception e) {
			e.printStackTrace();

			redirect.addFlashAttribute("error", "Ocurrió un error al momento de obtener los datos");
			return "/incidencias/home";
		}

		return "/incidencias/ver";
	}

	/**
	 * Método que busca al afiliado para poder crear la incidencia
	 * 
	 * @param model
	 * @return
	 */

	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/buscar")
	public String buscar(Model model) {

		LOG.info("Entra el método de buscar el afiliado por cualquier parámetro mostrado");

		Afiliado afiliado = new Afiliado();

		model.addAttribute("afiliado", afiliado);

		return "incidencias/buscar";
	}

	/**
	 * Método que realiza la búsqueda del afiliado con respecto al perfil y su
	 * centro de asistencia asosciado
	 * 
	 * @param campos
	 * @param model
	 * @param redirect
	 * @return
	 * @throws Exception
	 */
	
	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/resultado", method = RequestMethod.POST)
	public String resultado(@RequestParam(name = "campos[]") String[] campos, Model model, RedirectAttributes redirect)
			throws Exception {

		LOG.info("Método que realiza la búsqueda del Afiliado");

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuario = usuarioService.findByUsername(authentication.getName());

			List<AfiliadoCustom> afiliados = afiliadoService.getAfiliadoByParams(campos,
					usuario.getCentroContacto().getId());

			if (afiliados.size() == 0) {
				redirect.addFlashAttribute("error", "Registro no encontrado");
				return "redirect:/incidencias/buscar";
			}

			model.addAttribute("afiliados", afiliados);

		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Error al momento de realizar la búsqueda");

			LOG.error("Error al momento de realizar la búsqueda", e);

			return "redirect:/incidencias/buscar";
		}

		return "/incidencias/resultado";
	}

	/**
	 * Método para visualizar los datos del Afiliado y benficio y para agregar la
	 * incidencia al Afiliado
	 * 
	 * @param id
	 * @param model
	 * @return
	 */

	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/crear/{id}")
	public String crear(@PathVariable("id") Long id, Model model) {

		Incidencia incidencia = new Incidencia();

		try {
			Afiliado afiliado = afiliadoService.findById(id);
			List<IncidenciaCustom> historiales = incidenciaService.getHistorialIncidenciaByIdAfiliado(id);
			idAfiliado = id;

			historiales.forEach(historial -> {
				System.out.println(historial.getDetalle());
			});
	
			model.addAttribute("afiliado", afiliado);
			model.addAttribute("incidencia", incidencia);
			model.addAttribute("relServicioBeneficios", getBeneficioByAfiliado(afiliado));
			model.addAttribute("historiales", historiales);
		}catch(Exception e) {
			LOG.error("Error al momento de crear la incidencia", e);
			e.printStackTrace();
		}

		return "incidencias/crear";
	}

	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {

		try {

			Incidencia incidencia = new Incidencia();
			List<RelServicioBeneficio> relServicioBeneficio = new ArrayList<RelServicioBeneficio>();
			List<RelAfiliadoIncidenciaBeneficioCustom> relAfiliadoIncidenciaBeneficio = relAfiliadoIncidenciaBeneficioService
					.getRelAfiliadoIncidenciaBeneficioByIdIncidencia(id);

			System.out.println(relAfiliadoIncidenciaBeneficio.size());
			
			if (relAfiliadoIncidenciaBeneficio != null && relAfiliadoIncidenciaBeneficio.size() > 0) {

				Afiliado afiliado = afiliadoService.findById(relAfiliadoIncidenciaBeneficio.get(0).getIdAfiliado());

				// Lista para agregar la relación entre los beneficios del afiliado y la lista
				// de beneficios ya creada
				List<RelServicioBeneficio> relServicioBeneficios = new ArrayList<RelServicioBeneficio>();

				if (id > 0) {
					incidencia = incidenciaService.findById(id);
					if (incidencia == null) {

						LOG.info("No se ha encontrado la incidencia con ID: " + id);
						redirect.addFlashAttribute("error", "No se ha encontrado la incidencia");
						return "redirect:/incidencias/home";
					}
				} else {
					LOG.info("Incidencia con ID cero no existe");
					redirect.addFlashAttribute("error", "El ID de la incidencia no puede ser cero");

					return "redirect:/incidencias/home";
				}

				relServicioBeneficio = getBeneficioByAfiliado(afiliado);

				int index = 0;

				for (RelServicioBeneficio relSB : relServicioBeneficio) {

					System.out.println(relSB.getBeneficio().toString());

					if (relAfiliadoIncidenciaBeneficio.size() > index && relSB.getBeneficio().getId() ==
							relAfiliadoIncidenciaBeneficio.get(index).getIdBeneficio()) {

						RelServicioBeneficio beneficio = new RelServicioBeneficio(relSB.getServicio(),
								relSB.getBeneficio(), relSB.getTitular(), relSB.getBeneficiario(),
								relSB.getDescripcion());

						Beneficio nBeneficio = beneficioService.findById(relAfiliadoIncidenciaBeneficio.get(index)
								.getIdBeneficio());

						beneficio.setBeneficio(nBeneficio);
						relServicioBeneficios.add(beneficio);

						index++;
					} else {
						RelServicioBeneficio beneficio = new RelServicioBeneficio();
						Beneficio b = new Beneficio();
						b.setId(0L);
						beneficio.setBeneficio(b);
						relServicioBeneficios.add(beneficio);
					}
				}

				model.addAttribute("incidencia", incidencia);
				model.addAttribute("afiliado", afiliado);
				model.addAttribute("relServicioBeneficio", relServicioBeneficio);
				model.addAttribute("relServicioBeneficios", relServicioBeneficios);

				idAfiliado = afiliado.getId();

			}else {
				incidencia = incidenciaService.findById(id);
				
				String nombre = incidencia.getNombreAfiliado();
				String[] nombreCompleto = nombre.split(" ");
				
				System.out.println(nombreCompleto.length);
								
				if(nombreCompleto.length == 3) {
					
					System.out.println(nombreCompleto[0] + nombreCompleto[1] +  nombreCompleto[2]);
					
					Long claveAfiliado = afiliadoService.getIdAfiliadoByNombreCompleto(nombreCompleto[0], nombreCompleto[1], nombreCompleto[2]);
					Afiliado afiliado = afiliadoService.findById(claveAfiliado);
					
					model.addAttribute("afiliado", afiliado);
					model.addAttribute("incidencia", incidencia);
					
					idAfiliado = claveAfiliado;
				}	
				if(nombreCompleto.length == 4) {
					Long claveAfiliado = afiliadoService.getIdAfiliadoByNombreCompleto(nombreCompleto[0] + " " + nombreCompleto[1], nombreCompleto[2], nombreCompleto[3]);
					Afiliado afiliado = afiliadoService.findById(claveAfiliado);
					
					model.addAttribute("afiliado", afiliado);
					model.addAttribute("incidencia", incidencia);
					
					idAfiliado = claveAfiliado;
				}
			}

		} catch (Exception e) {

			LOG.error("Error en el módulo de editar", e);
			e.printStackTrace();
		}

		return "/incidencias/editar";

	}

	/**
	 * Método que guarda la incidencia
	 * 
	 * @param incidencia
	 * @param relServicioBeneficios
	 * @param redirect
	 * @param status
	 * @return
	 */

	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardar(Incidencia incidencia, @ModelAttribute RelServicioBeneficioDto relServicioBeneficios,
			RedirectAttributes redirect, SessionStatus status) {

		String messageStatus = null;
		
		try {

			List<RelServicioBeneficio> relServicioBeneficio = relServicioBeneficios.getRelServicioBeneficios();
			Afiliado afiliado = afiliadoService.findById(idAfiliado);

			System.out.println(incidencia.getDetalle());

			incidencia.setNombreAfiliado(afiliado.getNombre() + ' ' + afiliado.getApellidoPaterno() + ' '
					+ afiliado.getApellidoMaterno());

			if (incidencia.getId() == null) {
				incidencia.setEstatus(1);
				incidencia.setFechaCreacion(new Date());
			}

			DateFormat df = new SimpleDateFormat("HH:mm a");
			Date date = null;

			date = df.parse(incidencia.getHora());
			String hora = new SimpleDateFormat("H:mm:ss").format(date);
			incidencia.setHora(hora);

			String detalle = incidencia.getDetalle();
			detalle = detalle.replaceAll("\\<.*?\\>", "");
			incidencia.setDetalle(detalle);

			incidenciaService.save(incidencia);

			if(relServicioBeneficio != null) {

				RelAfiliadoIncidenciaBeneficio relAfiliadoIncidenciaBeneficio = new RelAfiliadoIncidenciaBeneficio();

				relServicioBeneficio.removeAll(Arrays.asList(null, null));

				for (RelServicioBeneficio r : relServicioBeneficio) {
					if (r.getBeneficio() != null) {
						relAfiliadoIncidenciaBeneficio.setIncidencia(incidencia);
						relAfiliadoIncidenciaBeneficio.setAfiliado(afiliado);
						relAfiliadoIncidenciaBeneficio.setBeneficio(r.getBeneficio());
						relAfiliadoIncidenciaBeneficio.setFecha(new Date());

						relAfiliadoIncidenciaBeneficioService.save(relAfiliadoIncidenciaBeneficio);
					}
				}
			}else{

				RelAfiliadoIncidencia relAfiliadoIncidencia = new RelAfiliadoIncidencia();

				relAfiliadoIncidencia.setIncidencia(incidencia);
				relAfiliadoIncidencia.setAfiliado(afiliado);
				relAfiliadoIncidencia.setFecha(new Date());

				relAfiliadoIncidenciaService.save(relAfiliadoIncidencia);
			}

			if(incidencia.getId() > 0){
				messageStatus = "Incidencia editada correctamente";
			}else{
				messageStatus = "Incidencia creada correctamente";
			}


//			if (relServicioBeneficio != null) {
//
//				relServicioBeneficio.removeAll(Arrays.asList(null, null));
//
//				incidencia.setNombreAfiliado(afiliado.getNombre() + ' ' + afiliado.getApellidoPaterno() + ' '
//						+ afiliado.getApellidoMaterno());
//
//				if (incidencia.getId() == null) {
//					incidencia.setEstatus(1);
//					incidencia.setFechaCreacion(new Date());
//				}
//
//				DateFormat df = new SimpleDateFormat("HH:mm a");
//				Date date = null;
//
//				date = df.parse(incidencia.getHora());
//				String hora = new SimpleDateFormat("H:mm:ss").format(date);
//				incidencia.setHora(hora);
//
//				String detalle = incidencia.getDetalle();
//				detalle = detalle.replaceAll("\\<.*?\\>", "");
//
//				incidencia.setDetalle(detalle);
//
//				incidenciaService.save(incidencia);
//
//				for (RelServicioBeneficio r : relServicioBeneficio) {
//
//					if (r.getBeneficio() != null) {
//						relAfiliadoIncidenciaBeneficio.setIncidencia(incidencia);
//						relAfiliadoIncidenciaBeneficio.setAfiliado(afiliado);
//						relAfiliadoIncidenciaBeneficio.setBeneficio(r.getBeneficio());
//						relAfiliadoIncidenciaBeneficio.setFecha(new Date());
//
//						relAfiliadoIncidenciaBeneficioService.save(relAfiliadoIncidenciaBeneficio);
//					}
//
//				}
//
//				messageStatus = "Incidencia editada correctamente";
//
//			} else {
//
//				if (incidencia.getId() == null) {
//					incidencia.setEstatus(1);
//					incidencia.setFechaCreacion(new Date());
//				}
//
//				DateFormat df = new SimpleDateFormat("HH:mm a");
//				Date date = null;
//
//				date = df.parse(incidencia.getHora());
//				String hora = new SimpleDateFormat("H:mm:ss").format(date);
//				incidencia.setHora(hora);
//
//				String detalle = incidencia.getDetalle();
//				detalle = detalle.replaceAll("\\<.*?\\>", "");
//
//				incidencia.setDetalle(detalle);
//
//				incidencia.setNombreAfiliado(afiliado.getNombre() + ' ' + afiliado.getApellidoPaterno() + ' '
//						+ afiliado.getApellidoMaterno());
//
//				incidenciaService.save(incidencia);
//
//				messageStatus = "Incidencia creada correctamente";
//			}

		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");
		}

		redirect.addFlashAttribute("success", messageStatus);
		return "redirect:/incidencias/ver";
	}

	@Secured("ROLE_ASISTENCIA")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirect) {

		if (id > 0) {
			incidenciaService.deleteById(id);
			redirect.addFlashAttribute("success", "El registro se ha eliminado correctamente");
		} else {
			redirect.addFlashAttribute("error", "El id no puede ser cero");
		}

		return "redirect:/incidencias/home";
	}

	/**
	 * Método que obtiene los beneficios asignados al Afiliado
	 * 
	 * @param afiliado
	 * @return
	 */

	public List<RelServicioBeneficio> getBeneficioByAfiliado(Afiliado afiliado) {

		List<RelServicioBeneficio> relServicioBeneficios = relServicioBeneficio
				.getBeneficioByIdAfiliado(afiliado.getId());
		return relServicioBeneficios;
	}

	@ModelAttribute("estatusIncidencia")
	public Map<Integer, String> getEstatusIncidencia() {

		Map<Integer, String> estatusIncidencia = new HashMap<Integer, String>();
		estatusIncidencia.put(1, "Abierto");
		estatusIncidencia.put(2, "En Proceso");
		estatusIncidencia.put(3, "Completado");
		estatusIncidencia.put(4, "Cancelado");

		return estatusIncidencia;

	}

}
