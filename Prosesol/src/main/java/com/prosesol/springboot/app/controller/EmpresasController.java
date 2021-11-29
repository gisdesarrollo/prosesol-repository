package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.Empresa;
import com.prosesol.springboot.app.service.IEmpresaService;
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

    @Secured({"ROLE_ADMINISTRADOR"})
    @RequestMapping(value = "/borrar", method = RequestMethod.DELETE)
    public String borrar(@ModelAttribute(name = "empresa") Empresa empresa, RedirectAttributes redirect){
        try{
            empresaService.delete(empresa);
        }catch (DataIntegrityViolationException dive){
            dive.printStackTrace();
            redirect.addFlashAttribute("error", "No se puede eliminar la empresa");

            return "redirect:/empresas/ver";
        }

        return "catalogos/empresas/ver";
    }

}
