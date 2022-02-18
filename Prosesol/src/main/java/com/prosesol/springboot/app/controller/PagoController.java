package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IPagoService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            //model.addAttribute("pagos", pagoService.getAllPagos());
        }catch(Exception e){
            LOG.error("Error al momento de mostrar la lista", e);
            e.printStackTrace();
        }

        return "catalogos/pagos/realizados";
    }
    
    @RequestMapping(value = "/buscar", method = RequestMethod.POST)
	public String buscar(@RequestParam(name = "fechaInicial") String fechaInicial,@RequestParam(name = "fechaFinal") String fechaFinal,
			@RequestParam(name = "formaPago") String formaPago , Model model, RedirectAttributes redirect)
			throws Exception {
    	
    	try {
    		DateFormat format = new SimpleDateFormat("dd/MM/yyyy"); 
    		DateFormat formatFechas = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date fechaI = format.parse(fechaInicial);
    		Date fechaF = format.parse(fechaFinal);
    		String FI = formatFechas.format(fechaI);
    		String FF = formatFechas.format(fechaF);
    		List<String> pagos = new ArrayList<String>();
    		if (formaPago.equals("seleccionar")) {
    			pagos = pagoService.getPagosByFechaInicialAndFechaFinal(FI, FF);
    			if(!pagos.isEmpty()) {
				model.addAttribute("pagos",pagos);
    			}else {
    				redirect.addFlashAttribute("error", "No se encontraron pagos con la fecha enviada");
    				return "redirect:/pagos/realizados";
    			}
			}else {
				
				pagos = pagoService.getPagosByFechasAndFormaPago(FI, FF, formaPago);
				if(!pagos.isEmpty()) {
				model.addAttribute("pagos",pagos);
				}else {
    				redirect.addFlashAttribute("error", "No se encontraron pagos con la fecha y forma de pago enviada");
    				return "redirect:/pagos/realizados";
    			}
			}

			//model.addAttribute("pagos", pagoService.getAllPagos());

		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Error al momento de realizar la búsqueda");

			LOG.error("Error al momento de realizar la búsqueda", e);

			return "redirect:/pagos/realizados";
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
