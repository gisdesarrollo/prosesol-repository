package com.prosesol.springboot.app.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prosesol.springboot.app.async.AsyncCargaMasiva;
import com.prosesol.springboot.app.async.AsyncConciliacion;
import com.prosesol.springboot.app.entity.LogCM;
import com.prosesol.springboot.app.exception.CustomValidatorExcelException;
import com.prosesol.springboot.app.service.ILogCMService;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;

@Controller
@RequestMapping("/conciliacion")
public class ConciliacionController {
	
	protected static final Log LOG = LogFactory.getLog(ConciliacionController.class);
	
	 @Autowired
	    private ReportesExcelImpl reportesExcelImpl;
	 
	  @Autowired
	    private AsyncConciliacion asyncConciliacion;
	  
	  @Autowired
	    private ILogCMService logCMService;
	  
	/**
     * Vista de la Carga de conciliación de pagos de afiliados
     * @param model
     * @param redirect
     * @return
     */
	@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @GetMapping("/afiliados")
    public String cargaConciliacionPagosAfiliados(Model model, RedirectAttributes redirect) {

        //List<LogCM> lLogCm = logCMService.findAllLogsAfiliados();

        try{
            model.addAttribute("reportes", logCMService.findAllLogsConciliacion());
        }catch(Exception e){
            e.printStackTrace();
            redirect.addFlashAttribute("error: ", "Error al momento de generar" +
                    "la tabla");
            return "redirect:/conciliacion/reportes";
        }

        return "conciliacion/pagosConciliacion";
    }
	/**
     * Descarga el archivo para afiliados 
     * @param response
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @GetMapping("/templateXlsx")
    public String descargarTemplate(HttpServletResponse response, RedirectAttributes redirect) {

        try {

          reportesExcelImpl.generarTemplateConciliacionPagosAfiliadoXlsx(response);

        } catch (CustomValidatorExcelException e) {

            redirect.addFlashAttribute("error", e.getMessage());
            LOG.info("Error: " + e.getMessage());
            return "redirect:/conciliacion/afiliados";

        }

        return null;
    }
    
    /**
     * Carga el archivo de conciliación de pagos de afiliados
     * @param file
     * @param response
     * @param redirect
     * @return
     */

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadConciliacionPagosAfiliados(@RequestParam("file") MultipartFile file, HttpServletResponse response,
							 RedirectAttributes redirect) {

        boolean isVigor = false;
        boolean isConciliacion = true;
        try {

        	  if(!file.isEmpty()){

        	      int indexOfName = file.getOriginalFilename().indexOf(".");
        	      String extension = file.getOriginalFilename().substring(file.getOriginalFilename()
                          .lastIndexOf(".") + 1);
        	      String nombreArchivo = null;

                  if(!extension.equals("csv")){
                      redirect.addFlashAttribute("error",
                              "El archivo no es de tipo CSV, favor de convertir" +
                                      " el archivo de tipo CSV");
                      return "redirect:/conciliacion/afiliados";
                  }

        	      if(indexOfName != -1){
        	          nombreArchivo = file.getOriginalFilename().substring(0, indexOfName);
                  }

                  LOG.info("File Size: " + file.getBytes().length);
                  LOG.info("File Type: " + file.getContentType());
                  LOG.info("File Name: " + file.getOriginalFilename());

				  byte[] bytes = file.getBytes();
				  asyncConciliacion.procesaArchivoAsync(isVigor,isConciliacion, nombreArchivo, bytes);


			  }else{
        	      redirect.addFlashAttribute("warning", "Por favor, seleccione" +
                          " un archivo");
        	      return "redirect:/conciliacion/afiliados";
              }

        } catch (Exception ne) {

            LOG.error("Formato incorrecto", ne);
            redirect.addFlashAttribute("error", "Error al momento de realizar la inserción de pagos");

            return "redirect:/conciliacion/afiliados";
        }

        redirect.addFlashAttribute("info", "El archivo se está procesando");
        return "redirect:/pagos/realizados";
    }
    
    /**
     * Descarga el reporte para Conciliación de pagos de afiliados
     * @param id
     * @param response
     */

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
