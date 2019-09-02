package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.async.AsyncCargaMasiva;
import com.prosesol.springboot.app.entity.LogCM;
import com.prosesol.springboot.app.service.ILogCMService;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/cargaMasiva")
public class CargaMasivaController {

    protected static final Log logger = LogFactory.getLog(CargaMasivaController.class);

    @Autowired
    private ReportesExcelImpl reportesExcelImpl;

    @Autowired
    private AsyncCargaMasiva asyncCargaMasiva;

    @Autowired
    private ILogCMService logCMService;

    @GetMapping("/afiliados")
    public String cargaMasiva(Model model, RedirectAttributes redirect) {

        List<LogCM> lLogCm = logCMService.findAll();

        try{

            model.addAttribute("reportes", lLogCm);

        }catch(Exception e){
            e.printStackTrace();
            redirect.addFlashAttribute("error: ", "Error al momento de generar" +
                    "la tabla");
            return "redirect:/cargaMasiva/reportes";
        }

        return "cargaMasiva/afiliados";
    }

    /**
     * Descargar Template Excel
     */

    @Secured("ROLE_ADMINISTRADOR")
    @GetMapping("/templateXlsx")
    public void descargarTemplate(HttpServletResponse response) {

        try {

            reportesExcelImpl.generarTemplateAfiliadoXlsx(response);

        } catch (Exception e) {

            e.printStackTrace();
            response.setStatus(0);

        }
    }

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadXlsx(@RequestParam("file") MultipartFile file, HttpServletResponse response,
							 RedirectAttributes redirect) {

        try {

        	  if(!file.isEmpty()){
				  System.out.println("File size: " + file.getBytes().length);
				  System.out.println("File Type: " + file.getContentType());

				  byte[] bytes = file.getBytes();
				  asyncCargaMasiva.procesaArchivoAsync(file.getName(), bytes);
			  }

        } catch (Exception ne) {

            logger.error("Formato incorrecto", ne);
            redirect.addFlashAttribute("error", "Error al momento de realizar la inserción masiva");

            return "redirect:/cargaMasiva/afiliados";
        }

        redirect.addFlashAttribute("info", "El archivo se está procesando");
        return "redirect:/afiliados/ver";

    }

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @GetMapping(value = "/reportes/{id}")
    public void downloadReporte(@PathVariable("id") Long id, HttpServletResponse response){

        LogCM logCM = logCMService.findById(id);

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=" + logCM.getNombre());

        byte[] bytes = logCM.getArchivo();

        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
