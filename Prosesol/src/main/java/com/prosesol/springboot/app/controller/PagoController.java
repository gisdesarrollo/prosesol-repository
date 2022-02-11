package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IPagoService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    protected final Log LOG = LogFactory.getLog(PagoController.class);

    @Autowired
    private IPagoService pagoService;

    @Autowired
    private IAfiliadoService afiliadoService;

    @RequestMapping(value = "/realizados")
    public String getPagosRealizados(Model model){

        try{
            model.addAttribute("pagos", pagoService.getAllPagos());
        }catch(Exception e){
            LOG.error("Error al momento de mostrar la lista", e);
            e.printStackTrace();
        }

        return "catalogos/pagos/realizados";
    }

    @RequestMapping(value = "/pendientes")
    public String getPagosPendientes(Model model){

        try{
            model.addAttribute("afiliados",
                    afiliadoService.getAfiliadosPagoPendiente());
        }catch(Exception e){
            LOG.error("Error al momento de mostrar la lista", e);
            e.printStackTrace();
        }

        return "catalogos/pagos/pendientes";
    }

}
