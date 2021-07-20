package com.prosesol.springboot.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.CentroContacto;
import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.dao.IUsuarioDao;
import com.prosesol.springboot.app.service.ICentroContactoService;
import com.prosesol.springboot.app.service.IPerfilService;
import com.prosesol.springboot.app.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/usuarios")
public class UsuarioController {

	protected final Log LOG = LogFactory.getLog(this.getClass());

	@Value("${app.password}")
	private String password;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IPerfilService perfilService;

	@Autowired
	private ICentroContactoService centroContactoService;

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model) {

		LOG.info("Entra al método de ver usuario");

		try {
			model.addAttribute("titulo", "Usuarios");
			model.addAttribute("usuario", usuarioService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "catalogos/usuarios/ver";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		LOG.info("Entra al método crear usuario");

		Usuario usuario = new Usuario();

		model.put("usuario", usuario);
		model.put("titulo", "Crear Usuario");

		return "catalogos/usuarios/crear";

	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirect,
			SessionStatus status) {

		String passwordUser = null;
		try {

			if (result.hasErrors()) {
				model.addAttribute("titulo", "Crear Usuario");
				return "catalogos/usuarios/editar";
			}

			if (usuario.getId() != null) {
				LOG.info("Registro: " + usuario.getNombre() + " editado con éxito");
				
			} else {
				Usuario username = usuarioDao.findByUsername(usuario.getUsername().toString());
				if (username == null) {

					usuario.setEstatus(true);
					LOG.info("Registro creado con éxito");

					for (int i = 0; i < 2; i++) {
						passwordUser = passwordEncoder.encode(password);
					}
					usuario.setPassword(passwordUser);
				} else {
					redirect.addFlashAttribute("error", "Error el usuario ya se encuentra registrado");
					return "redirect:/usuarios/ver";
				}
			}

			usuarioService.save(usuario);
			status.setComplete();
			redirect.addFlashAttribute("success", "Registro creado correctamente");
		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Error al momento de insertar");
			return "redirect:/usuarios/ver";
		}
		return "redirect:/usuarios/ver";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findById(id);
			if (usuario == null) {
				redirect.addFlashAttribute("Error: ", "El id del usuario no existe");
				return "redirect:/usuarios/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del usuario no puede ser cero");
			return "redirect:/usuarios/ver";
		}

		model.put("usuario", usuario);
		model.put("titulo", "Editar usuario");

		return "catalogos/usuarios/editar";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO" })
	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {

		if (id > 0) {
			usuarioService.delete(id);
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}

		return "redirect:/usuarios/ver";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO", "ROLE_ASISTENCIA" })
	@RequestMapping(value = "/password")
	public String password(Model model) {

		Usuario usuario = new Usuario();

		model.addAttribute("usuario", usuario);

		return "/catalogos/usuarios/password";
	}

	@Secured({ "ROLE_ADMINISTRADOR", "ROLE_USUARIO", "ROLE_ASISTENCIA" })
	@RequestMapping(value = "/cambiar", method = RequestMethod.POST)
	public String cambiar(@ModelAttribute(name = "username") String username,
			@ModelAttribute(name = "password") String password, RedirectAttributes redirect) {

		String passwordUser = null;
		Usuario usuario = new Usuario();

		try {

			LOG.info("USERNAME: " + username);
			LOG.info("PASSWORD: " + password);

			usuario = usuarioService.findByUsername(username);
			for (int i = 0; i < 2; i++) {
				passwordUser = passwordEncoder.encode(password);
			}

			usuario.setPassword(passwordUser);

			LOG.info("USERNAME: " + username);

			usuarioService.save(usuario);

		} catch (Exception e) {
			e.printStackTrace();
		}

		redirect.addFlashAttribute("success", "Contraseña modificada exitosamente");

		return "redirect:/home";
	}

	/**
	 * Método para mostrar los perfiles Dentro del list box de crear usuario
	 */

	@ModelAttribute("litaPerfiles")
	public List<Perfil> listaPerfiles() {
		return perfilService.findAll();
	}

	@ModelAttribute("centros")
	public List<CentroContacto> listaCentros() {
		return centroContactoService.findAll();
	}
}
