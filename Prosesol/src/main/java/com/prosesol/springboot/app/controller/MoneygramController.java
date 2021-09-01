package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import com.prosesol.springboot.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/moneygram")
@SessionAttributes("{afiliado, relAfiliadoMoneygram}")
public class MoneygramController {

    protected final long ID_MONEYGRAM = 1L;

    protected final int PADDING_SIZE = 10;

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

    @Autowired
    private IParametroService parametroService;

    @Autowired
    private IEmpresaService empresaService;

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

    /**
     * Método para afiliar a un contratante al programa de moneygram
     * @param afiliado
     * @param relAfiliadoMoneygram
     * @param bindingResult
     * @param model
     * @param redirect
     * @param status
     * @return
     */

    @Secured({"ROLE_PROMOTOR"})
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    public String guardar(@ModelAttribute("afiliado") Afiliado afiliado,
                          @ModelAttribute("relAfiliadoMoneygram") RelAfiliadoMoneygram relAfiliadoMoneygram,
                          BindingResult bindingResult, Model model, RedirectAttributes redirect,
                          SessionStatus status){

        try{

            Empresa empresa = empresaService.findById(afiliado.getPromotor().getEmpresa().getId());
            Parametro parametro = parametroService.findById(ID_MONEYGRAM);
            String emailAfiliado = afiliado.getEmail();
            String emailContratante = relAfiliadoMoneygram.getEmail();

            if(empresa == null || parametro == null){
                redirect.addFlashAttribute("error", "El id de la empresa no se ha encontrado");
                return "redirect:/moneygram/crear";
            }

            String valor = parametro.getValor();
            String clave = empresa.getClave();
            String clavePromotor = afiliado.getPromotor().getClave();
            Long consecutivoEmpresa = empresa.getConsecutivo();

            // Verificar si la empresa trae un consecutivo
            if(consecutivoEmpresa == null){
                consecutivoEmpresa = 1L;
            }else{
                consecutivoEmpresa = consecutivoEmpresa + 1;
            }

            empresa.setConsecutivo(consecutivoEmpresa);
            empresaService.save(empresa);

            String consecutivo = String.format("%0" + PADDING_SIZE + "d", consecutivoEmpresa);

            afiliado.setEmail(emailAfiliado);
            afiliadoService.save(afiliado);

            String idMoneygram = valor + clave + clavePromotor + consecutivo;
            relAfiliadoMoneygram.setAfiliado(afiliado);
            relAfiliadoMoneygram.setIdMoneygram(idMoneygram);
            relAfiliadoMoneygram.setEmail(emailContratante);
            relAfiliadoMoneygramService.save(relAfiliadoMoneygram);

            status.setComplete();

        }catch(Exception e){
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Ocurrió un problema en el sistema, contacte al administrador");

            return "redirect:/moneygram/home";
        }

        return "redirect:/moneygram/ver";
    }

    @Secured({"ROLE_PROMOTOR"})
    @GetMapping(value = "/ver")
    public String ver(Model model, Authentication authentication){

        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        authorities.forEach(authority ->{
            if(authority.getAuthority().equals("ROLE_PROMOTOR")){
                model.addAttribute("afiliadoMoneygram", relAfiliadoMoneygramService.getAfiliadosByUsername(username));
            }
        });



        return "moneygram/ver";
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
