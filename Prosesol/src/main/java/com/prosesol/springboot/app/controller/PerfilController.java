package com.prosesol.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.service.IPerfilService;
import com.prosesol.springboot.app.service.IRoleService;

@Controller
@SessionAttributes("perfil")
@RequestMapping("/perfiles")
public class PerfilController {

	protected final Log LOG = LogFactory.getLog(this.getClass());

	@Autowired
	private IPerfilService perfilService;

	@Autowired
	private IRoleService roleService;

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@GetMapping(value = "/ver")
	public String ver(Model model) {

		model.addAttribute("titulo", "Perfiles");
		model.addAttribute("perfil", perfilService.findAll());

		LOG.info("Datos encontrados " + perfilService.findAll().toString());

		return "catalogos/perfiles/ver";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Perfil perfil = new Perfil();

		model.put("titulo", "Crear Perfil");
		model.put("roles", roleService.findAll());
		model.put("perfil", perfil);

		return "catalogos/perfiles/crear";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Perfil perfil, BindingResult result, Model model,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Perfil");
			return "catalogos/perfiles/crear";
		}

		if (perfil.getId() != null) {
			LOG.info("Perfil editado con ??xito");
		} else {
			perfil.setEstatus(true);
			LOG.info("Perfil creado con ??xito");
		}

		perfilService.save(perfil);
		status.setComplete();

		return "redirect:/perfiles/ver";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {

		Perfil perfil = null;

		try {
			if (id > 0) {
				perfil = perfilService.findById(id);
				if (perfil == null) {
					model.put("error", "El perfil no existe, ingrese otro id");
					return "redirect:/perfiles/ver";
				}
			} else {
				model.put("error", "No existe el perfil con id cero");
			}

			model.put("titulo", "Editar perfil");
			model.put("roles", roleService.findAll());
			model.put("perfil", perfil);

		} catch (Exception e) {
			model.put("error", "Error al momento de buscar el perfil");
			return "redirect:/perfiles/ver";
		}
		return "catalogos/perfiles/editar";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/borrar/{id}")
	public String borrar(@PathVariable("id") Long id) {

		LOG.info("Id de Perfil: " + id);

		if (id > 0) {

			perfilService.deleteById(id);
			LOG.info("El perfil se ha borrado correctamente");
		}

		return "redirect:/perfiles/ver";
	}

}
