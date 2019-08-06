package com.prosesol.springboot.app.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.entity.dao.IUsuarioDao;
import com.prosesol.springboot.app.entity.dto.RelServicioBeneficioDto;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IIncidenciaService;
import com.prosesol.springboot.app.service.IRelAfiliadoIncidenciaService;
import com.prosesol.springboot.app.service.IRelServicioBeneficioService;

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
	private IUsuarioDao usuarioDao;

	@Autowired
	private IRelServicioBeneficioService relServicioBeneficio;

	@Autowired
	private IRelAfiliadoIncidenciaService relAfiliadoIncidenciaService;

	private Long idAfiliado;
	
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

	@GetMapping(value = "/ver")
	public String ver(Model model, RedirectAttributes redirect) {

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();

			List<Incidencia> incidencias = incidenciaService.getIncidenciasByUserName(username);

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
	@RequestMapping(value = "/resultado", method = RequestMethod.POST)
	public String resultado(@RequestParam(name = "campos[]") String[] campos, Model model, RedirectAttributes redirect)
			throws Exception {

		LOG.info("Método que realiza la búsqueda del Afiliado");

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Usuario usuario = usuarioDao.findByUsername(authentication.getName());

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

	@RequestMapping(value = "/crear/{id}")
	public String crear(@PathVariable("id") Long id, Model model) {

		Incidencia incidencia = new Incidencia();

		try {
			Afiliado afiliado = afiliadoService.findById(id);
			idAfiliado = id;
	
			model.addAttribute("afiliado", afiliado);
			model.addAttribute("incidencia", incidencia);
			model.addAttribute("relServicioBeneficios", getBeneficioByAfiliado(afiliado));
		}catch(Exception e) {
			LOG.error("Error al momento de crear la incidencia", e);
			e.printStackTrace();
		}

		return "incidencias/crear";
	}

	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {

		try {

			Incidencia incidencia = new Incidencia();
			List<RelServicioBeneficio> relServicioBeneficio = new ArrayList<RelServicioBeneficio>();
			List<RelAfiliadoIncidencia> relAfiliadoIncidencia = relAfiliadoIncidenciaService
					.getRelAfiliadoIncidenciaByIdIncidencia(id);

			System.out.println(relAfiliadoIncidencia.size());
			
			if (relAfiliadoIncidencia.size() > 0) {

				Afiliado afiliado = afiliadoService.findById(relAfiliadoIncidencia.get(0).getAfiliado().getId());

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

					if (relAfiliadoIncidencia.size() > index && relSB.getBeneficio().getId() == relAfiliadoIncidencia.get(index).getBeneficio().getId()) {

						RelServicioBeneficio beneficio = new RelServicioBeneficio(relSB.getServicio(),
								relSB.getBeneficio(), relSB.getTitular(), relSB.getBeneficiario(),
								relSB.getDescripcion());
						beneficio.setBeneficio(relAfiliadoIncidencia.get(index).getBeneficio());
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

	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardar(@Valid Incidencia incidencia, @ModelAttribute RelServicioBeneficioDto relServicioBeneficios,
			RedirectAttributes redirect, SessionStatus status) {

		try {

			List<RelServicioBeneficio> relServicioBeneficio = relServicioBeneficios.getRelServicioBeneficios();
			RelAfiliadoIncidencia relAfiliadoIncidencia = new RelAfiliadoIncidencia();
			Afiliado afiliado = afiliadoService.findById(idAfiliado);

			if (relServicioBeneficio != null) {

				relServicioBeneficio.removeAll(Arrays.asList(null, null));

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

				incidenciaService.save(incidencia);

				for (RelServicioBeneficio r : relServicioBeneficio) {

					if (r.getBeneficio() != null) {
						relAfiliadoIncidencia.setIncidencia(incidencia);
						relAfiliadoIncidencia.setAfiliado(afiliado);
						relAfiliadoIncidencia.setBeneficio(r.getBeneficio());
						relAfiliadoIncidencia.setFecha(new Date());

						relAfiliadoIncidenciaService.save(relAfiliadoIncidencia);
					}

				}
			} else {
				
				if (incidencia.getId() == null) {
					incidencia.setEstatus(1);
					incidencia.setFechaCreacion(new Date());
				}
				
				DateFormat df = new SimpleDateFormat("HH:mm a");
				Date date = null;
				
				date = df.parse(incidencia.getHora());
				String hora = new SimpleDateFormat("H:mm:ss").format(date);
				incidencia.setHora(hora);
				
				incidencia.setNombreAfiliado(afiliado.getNombre() + ' ' + afiliado.getApellidoPaterno() + ' '
						+ afiliado.getApellidoMaterno());

				incidenciaService.save(incidencia);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/incidencias/ver";
	}

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
	 * @param servicio
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
