package com.prosesol.springboot.app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
		List<String> estados = afiliadoService.getAllEstados();
		
		
		model.put("afiliado", afiliado);
		model.put("estados", estados);
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
		
		model.put("afiliado", afiliadoService.findById(id));	
		model.put("afiliados", beneficiarios);
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
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid Afiliado afiliado, BindingResult result, Model model, RedirectAttributes redirect,
			SessionStatus status) {

		System.out.println("Entra al método guardar");
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Afiliado");
			return "catalogos/afiliados/crear";
		}

		String mensajeFlash = (afiliado.getId() != null) ? "Registro editado con éxito" : "Registro creado con éxito";

		afiliado.setEstatus(true);
		afiliado.setIsBeneficiario(false);
		
		afiliadoService.save(afiliado);
		status.setComplete();
		redirect.addFlashAttribute("success", mensajeFlash);

		return "redirect:/afiliados/ver";
	}

	@RequestMapping(value = "/ver", method = RequestMethod.GET)
	public String ver(Model model, Authentication authentication) {
		
		Set<Beneficiario> beneficiarios = new HashSet<Beneficiario>();
		
		if (authentication != null) {
			logger.info("Usuario autenticado: ".concat(authentication.getName()));
		}		
		
		for(Afiliado afiliado : afiliadoService.findAll()) {
			beneficiarios = afiliado.getBeneficiarios();
			for(Beneficiario beneficiario : beneficiarios) {
				System.out.println(beneficiario.getAfiliado().getNombre());
			}
		}
		
		
		
		System.out.println(afiliadoService.findAll());
		
		model.addAttribute("titulo", "Afiliados");
		model.addAttribute("afiliados", afiliadoService.findAll());

		return "catalogos/afiliados/ver";

	}	
	
	public void agregarAfiliado(Afiliado afiliado) {
		
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
