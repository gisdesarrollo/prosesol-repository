package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import com.prosesol.springboot.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/moneygram")
@SessionAttributes("{afiliado, relAfiliadoMoneygram}")
public class MoneygramController {

    @Autowired
    private IRelAfiliadoMoneygramService relAfiliadoMoneygramService;

    @Autowired
    private IPeriodicidadService periodicidadService;

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private IServicioService servicioService;

    @Autowired
    private ICuentaService cuentaService;

    @Autowired
    private IPromotorService promotorService;

    @Secured("ROLE_PROMOTOR")
    @GetMapping(value = "/home")
    public String home() {
        return "moneygram/home";
    }

    @Secured({"ROLE_PROMOTOR"})
    @RequestMapping(value = "/crear")
    public String crear(Map<String, Object> model){

        Afiliado afiliado = new Afiliado();

        model.put("afiliado", afiliado);
        model.put("relAfiliadoMoneygram", new RelAfiliadoMoneygram());

        return "moneygram/crear";
    }

    @Secured({"ROLE_PROMOTOR"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String guardar(@ModelAttribute("afiliado") Afiliado afiliado,
                          @ModelAttribute("relAfiliadoMoneygram") RelAfiliadoMoneygram relAfiliadoMoneygram,
                          BindingResult bindingResult, Model model, RedirectAttributes redirect,
                          SessionStatus status){

        try{

            afiliadoService.save(afiliado);
            long idMoneygram = 123456789123456L;
            relAfiliadoMoneygram.setAfiliado(afiliado);
            relAfiliadoMoneygram.setIdMoneygram(idMoneygram);
            relAfiliadoMoneygramService.save(relAfiliadoMoneygram);

            status.setComplete();

        }catch(Exception e){
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");

            return "redirect:/moneygram/home";
        }

        return "redirect:/moneygram/home";
    }

    /**
     * Método para mostrar los periodos Dentro del list box de crear afiliados
     *
     * @param(name ="periodos")
     */

    @ModelAttribute("periodos")
    public List<Periodicidad> listaPeriodos() {
        return periodicidadService.findAll();
    }

    /**
     * Método para mostrar los estados Dentro del list box de crear afiliados
     *
     * @param(name = "estados")
     */

    @ModelAttribute("estados")
    public List<String> getAllEstados() {
        return afiliadoService.getAllEstados();
    }

    /**
     * Método para mostrar los servicios Dentro del list box de crear afiliados
     *
     * @param(name = "servicios")
     */

    @ModelAttribute("servicios")
    public List<Servicio> getAllServicios() {
        return servicioService.findAll();
    }

    /**
     * Método para mostrar los servicios Dentro del list box de crear afiliados
     *
     * @param(name = "promotores")
     */

    @ModelAttribute("promotores")
    public List<Promotor> getAllPromotores() {
        return promotorService.findAll();
    }

    /**
     * Método para mostrar los servicios Dentro del list box de crear afiliados
     *
     * @param(name = "cuentas")
     */

    @ModelAttribute("cuentas")
    public List<Cuenta> getAllCuentas() {
        return cuentaService.findAll();
    }

}
