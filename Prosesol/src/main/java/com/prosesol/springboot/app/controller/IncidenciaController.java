package com.prosesol.springboot.app.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.entity.dao.IRelAfiliadoIncidenciaDao;
import com.prosesol.springboot.app.entity.dao.IUsuarioDao;
import com.prosesol.springboot.app.entity.dto.RelServicioBeneficioDto;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IIncidenciaService;
import com.prosesol.springboot.app.service.IRelServicioBeneficioService;
import com.prosesol.springboot.app.service.IServicioService;

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
	private IServicioService servicioService;

	@Autowired
	private IRelServicioBeneficioService relServicioBeneficio;
	
	@Autowired
	private IRelAfiliadoIncidenciaDao relAfiliadoIncidenciaDao;
	
	private Long idAfiliado;

	/**
	 * Método que busca al afiliado para poder crear la incidencia
	 * 
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/buscar")
	public String crear(Model model) {

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
	public String buscar(@RequestParam(name = "campos[]") String[] campos, Model model, RedirectAttributes redirect)
			throws Exception {

		LOG.info("Método que realiza la búsqueda del Afiliado");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = usuarioDao.findByUsername(authentication.getName());

		List<AfiliadoCustom> afiliados = afiliadoService.getAfiliadoByParams(campos,
				usuario.getCentroContacto().getId());

		try {

			if (afiliados.size() == 0) {
				redirect.addFlashAttribute("error", "Registro no encontrado");
				return "redirect:/incidencias/buscar";
			}

		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Error al momento de realizar la búsqueda");

			LOG.error("Error al momento de realizar la búsqueda", e);

			return "redirect:/incidencias/buscar";
		}

		model.addAttribute("afiliados", afiliados);

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

		Afiliado afiliado = afiliadoService.findById(id);
		idAfiliado = id;
		Servicio servicio = servicioService.findById(afiliado.getServicio().getId());

		model.addAttribute("afiliado", afiliado);
		model.addAttribute("incidencia", incidencia);
		model.addAttribute("relServicioBeneficios", getBeneficioByAfiliado(servicio));

		return "incidencias/crear";
	}

	@RequestMapping(value = "/guardar", method = RequestMethod.POST)
	public String guardar(@Valid Incidencia incidencia,	@ModelAttribute RelServicioBeneficioDto relServicioBeneficios, 
								 RedirectAttributes redirect, SessionStatus status) {
		
		List<RelServicioBeneficio> relServicioBeneficio = relServicioBeneficios.getRelServicioBeneficios();
		RelAfiliadoIncidencia relAfiliadoIncidencia = new RelAfiliadoIncidencia();
		Afiliado afiliado = afiliadoService.findById(idAfiliado);
		
		System.out.println(afiliado.getNombre());
		
		try {
			
		
			incidencia.setNombreAfiliado("Nombre");
			incidencia.setEstatus(1);
			
			incidenciaService.save(incidencia);
		
			for(RelServicioBeneficio r : relServicioBeneficio) {
				
				if(r.getBeneficio() != null) {
					relAfiliadoIncidencia.setIncidencia(incidencia);
					relAfiliadoIncidencia.setAfiliado(afiliado);
					relAfiliadoIncidencia.setBeneficio(r.getBeneficio());	
					relAfiliadoIncidencia.setFecha(new Date());
				}
				
			}
			
			relAfiliadoIncidenciaDao.save(relAfiliadoIncidencia);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "/homeAsistencia";
	}

	/**
	 * Método que obtiene los beneficios asignados al Afiliado
	 * 
	 * @param servicio
	 * @return
	 */

	public List<RelServicioBeneficio> getBeneficioByAfiliado(Servicio servicio) {

		List<RelServicioBeneficio> relServicioBeneficios = relServicioBeneficio
				.getRelServicioBeneficioByIdServicio(servicio.getId());
		return relServicioBeneficios;
	}

	
}
