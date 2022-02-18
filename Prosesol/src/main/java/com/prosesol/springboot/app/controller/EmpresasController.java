package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.Empresa;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.service.IEmpresaService;
import com.prosesol.springboot.app.service.IPromotorService;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empresas")
@SessionAttributes(names = {"empresa"})
public class EmpresasController {

    protected final Log LOG = LogFactory.getLog(this.getClass());

    @Autowired
    private IEmpresaService empresaService;
    


    @Secured({"ROLE_ADMINISTRADOR"})
    @RequestMapping(value = "/crear")
    public String crear(Model model){
        Empresa empresa = new Empresa();

        model.addAttribute("empresa", empresa);

        return "catalogos/empresas/crear";
    }

    @Secured({"ROLE_ADMINISTRADOR"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String crear(@ModelAttribute(name = "empresa")Empresa empresa,
                        SessionStatus status, RedirectAttributes redirectAttributes){
    	//posiblemete sirva para dar mensaje de editar
        try {

            if(empresa.getId() != null){
                LOG.info("Empresa editada con éxito");
            }else {
                LOG.info("Empresa creada con éxito");
            }

            empresaService.save(empresa);

            status.setComplete();
        }catch (Exception e){
            e.printStackTrace();
            return "catalogos/empresas/crear";
        }

        return "redirect:/empresas/ver";

    }


    @Secured({"ROLE_ADMINISTRADOR"})
    @GetMapping(value = "/ver")
    public String ver(Model model){
        model.addAttribute("empresas", empresaService.findAll());

        return "catalogos/empresas/ver";
    }
    
    //evelio
    @Secured({"ROLE_ADMINISTRADOR"})
    @RequestMapping(value = "borrar/{id}")
    public String borrar(@PathVariable(value = "id") Long id, RedirectAttributes redirect){
        try{
        	if(id>0) {
        		empresaService.delete(id);
        		redirect.addFlashAttribute("success", "Registro eliminado correctamente");
        	}
            
        }catch (DataIntegrityViolationException dive){
            dive.printStackTrace();
            redirect.addFlashAttribute("error", "No se puede eliminar la empresa");
            return "redirect:/empresas/ver";
        }
        return "redirect:/empresas/ver";
    }
    @Secured({"ROLE_ADMINISTRADOR"})
	@RequestMapping(value = "/editar/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes redirect) {

    	Empresa empresa = null;

		if (id > 0) {
			empresa = empresaService.findById(id);
			if (empresa == null) {
				redirect.addFlashAttribute("Error: ", "El id del promotor no existe");
				return "redirect:/empresas/ver";
			}
		} else {
			redirect.addFlashAttribute("Error: ", "El id del promotor no puede ser cero");
			return "redirect:/empresas/ver";
		}

		model.put("empresa", empresa);
		
		return "catalogos/empresas/editar";
		

	}

}
