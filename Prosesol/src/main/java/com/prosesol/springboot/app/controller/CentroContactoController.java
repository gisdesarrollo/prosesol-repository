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

import com.prosesol.springboot.app.entity.CentroContacto;
import com.prosesol.springboot.app.service.CentroContactoServiceImpl;

@Controller
@SessionAttributes("centroContacto")
@RequestMapping("/centros")
public class CentroContactoController {

	protected static final Log LOG = LogFactory.getLog(CentroContacto.class);
	
	@Autowired
	private CentroContactoServiceImpl centroContactoService;
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear")
	public String crear(Model model) {
		
		LOG.info("Entra al método de crear Centro de Asistencia");
		
		CentroContacto centroContacto = new CentroContacto();
		
		model.addAttribute("centroContacto", centroContacto);
		
		return "catalogos/centros/crear";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@GetMapping(value = "/ver")
	public String ver(Model model) {
		
		LOG.info("Entra al método de ver Centro Asistencia");
		
		model.addAttribute("centros", centroContactoService.findAll());
		
		return "catalogos/centros/ver";
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/crear", method = RequestMethod.POST)
	public String guardar(@Valid CentroContacto centroContacto, BindingResult result, Model model, 
						  RedirectAttributes redirect,  SessionStatus status) {
		
		LOG.info("Entra al método de guardar");
		
		if(result.hasErrors()) {
			
			LOG.error("Error de usuario");
			return "redirect:/centros/ver";
			
		}
				
		String flashMessage = (centroContacto.getId() != null) ? "El registro se ha editado correctamente" : "El registro se ha creado correctamente";
		
		centroContacto.setEstatus(true);
		
		centroContactoService.save(centroContacto);
		status.setComplete();
		
		redirect.addFlashAttribute("success", flashMessage);
		
		LOG.info("Centro con id: " + centroContacto.getId() + " guardado correctamente");
		
		return "redirect:/centros/ver";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes redirect) {
		
		CentroContacto centroContacto = null;
		
		if(id > 0) {
			centroContacto = centroContactoService.findById(id);
			
			if(centroContacto == null) {
				redirect.addFlashAttribute("error", "El registro no se ha encontrado");
				LOG.info("El registro con id: " + id + " no se ha encontrado");
				
				return "redirect:/centros/ver";
			}
		}else {
			redirect.addFlashAttribute("error", "El id no puede ser cero");
			LOG.info("El id no puede ser cero");
			
			return "redirect:/centros/ver";
		}
		
		model.put("centroContacto", centroContacto);
		
		LOG.info("Registro: " + centroContacto.getNombre() + " editado correctamente");
		
		return "catalogos/centros/editar";
		
	}
	
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	@RequestMapping(value = "/eliminar/{id}")
	public String borrar(@PathVariable("id")Long id,  RedirectAttributes redirect) {
		
		LOG.info("Entra al método de eliminar registro de contacto");
		LOG.info("Registro a eliminar: " + centroContactoService.findById(id));
		
		if(id > 0) {
			centroContactoService.deleteById(id);
			LOG.info("Registro eliminado");
		}else {
			redirect.addFlashAttribute("error", "El id no puede ser cero");
		}
		
		redirect.addFlashAttribute("success", "Registro eliminado correctamente");
		
		return "redirect:/centros/ver";
		
	}
}
