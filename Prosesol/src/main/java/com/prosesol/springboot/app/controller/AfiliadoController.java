package com.prosesol.springboot.app.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficiario;
import com.prosesol.springboot.app.service.IAfiliadoService;

@Controller
@SessionAttributes("afiliado")
@RequestMapping("/afiliados")
public class AfiliadoController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IAfiliadoService afiliadoService;

	@RequestMapping(value = "/crear")
	public String crear(Map<String, Object> model) {

		Afiliado afiliado = new Afiliado();

		model.put("afiliado", afiliado);
		model.put("titulo", "Crear Afiliado");

		return "catalogos/afiliados/crear";
	}

	@GetMapping(value = "/detalle/{id}")
	public String detalle(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {
		
		Afiliado afiliado = afiliadoService.findById(id);
		List<Afiliado> beneficiarios = afiliadoService.getBeneficiarioByIdByIsBeneficiario(id);
		
		if(afiliado == null) {
			redirect.addFlashAttribute("error", "El id del afiliado no existe");
			return "redirect:/afiliados/ver";
		}
		
		model.put("afiliado", afiliado);
				
		for(Afiliado beneficiario : beneficiarios) {
			System.out.println(beneficiarios.toString());
			model.put("beneficiarios", beneficiario);
		}
		
		System.out.println(afiliado.toString());
		model.put("titulo", "Detalle Afiliado" + ' ' + afiliado.getNombre());
		
		return "catalogos/afiliados/detalle";
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

		System.out.println("Entra al método editar");
		
		Afiliado afiliado = null;

		if (id > 0) {
			afiliado = afiliadoService.findById(id);
			if (afiliado == null) {
				redirect.addFlashAttribute("Error: ", "El id del afiliado no existe");
				return "redirect:/afiliados/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del afiliado no puede ser cero");
			return "redirect:/afiliados/ver";
		}

		model.put("afiliado", afiliado);
		model.put("titulo", "Editar afiliado");


		System.out.println("Fin de editar");
		
		return "catalogos/afiliados/editar";
		

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/crear", params="crearAfiliado", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model, RedirectAttributes redirect,
			SessionStatus status) {

		System.out.println("Entra al método guardar");
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Afiliado");
			return "catalogos/afiliados/crear";
		}

		String mensajeFlash = (afiliado.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";

		afiliadoService.save(afiliado);
		status.setComplete();
		redirect.addFlashAttribute("success", mensajeFlash);

		return "redirect:/afiliados/ver";
	}

	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model, Authentication authentication, HttpServletRequest request) {

		if (authentication != null) {
			logger.info("Usuario autenticado: ".concat(authentication.getName()));
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			logger.info("Utilizando forma estática, usuario autenticado: ".concat(auth.getName()));
		}

		if (hasRole("ROLE_ADMIN")) {
			logger.info("Accesso garantizado ".concat(auth.getName()));
		} else {
			logger.info("Accesso denegado ".concat(auth.getName()));
		}

		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"");

		if (securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando SecurityContextHolderAwareRequest Accesso garantizado ".concat(auth.getName()));
		} else {
			logger.info("Forma usando SecurityContextHolderAwareRequest Accesso denegado ".concat(auth.getName()));
		}

		if (request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando HttpServletRequest Accesso garantizado ".concat(auth.getName()));
		} else {
			logger.info("Forma usando HttpServletRequest Accesso denegado ".concat(auth.getName()));
		}

		model.addAttribute("titulo", "Afiliados");
		model.addAttribute("afiliado", afiliadoService.findAll());

		return "catalogos/afiliados/ver";

	}

	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));
//		for(GrantedAuthority authority : authorities) {
//			if(role.equals(authority.getAuthority())) {
//				logger.info("Hola usuario: ".concat(auth.getName()).concat(" tu rol es: ".concat(authority.getAuthority())));
//				return true;
//			}
//		}
//		
//		return false;

	}

	@RequestMapping(value = "/crear", params="agregarBeneficiario", method = RequestMethod.POST)
	public String crearAfiliado( @ModelAttribute("beneficiario") Beneficiario beneficiario, @Valid Afiliado afiliado, BindingResult result, Model model,
			RedirectAttributes redirect, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Afiliado");
			return "catalogos/afiliados/crear";
		}
		
		this.guardar(afiliado, result, model, redirect, status);
		
		model.addAttribute("beneficiario", new Beneficiario());
		
		System.out.println("Entrar al método crear afiliado desde el button Agregar Afiliado");
				
		model.addAttribute("titulo", "Agregar Beneficiario");		

		return "redirect:/beneficiarios/crear/" + afiliado.getId();
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirect) {
		
		logger.info("Entra el método de eliminación de afiliado");
		
		if(id > 0) {
			afiliadoService.delete(id);
			
			logger.info("El registro con el id: " + id + " se eliminará");
			redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		}
		
		return "redirect:/afiliados/ver";
	}
	
	
}
