package com.prosesol.springboot.app.controller;

import com.prosesol.springboot.app.async.AsyncCargaMasiva;
import com.prosesol.springboot.app.view.excel.ReportesExcelImpl;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

@Controller
@RequestMapping("/cargaMasiva")
public class CargaMasivaController {

    protected static final Log logger = LogFactory.getLog(CargaMasivaController.class);

    @Autowired
    private ReportesExcelImpl reportesExcelImpl;

    @Autowired
    private AsyncCargaMasiva asyncCargaMasiva;

    @GetMapping("/afiliados")
    public String cargaMasiva() {
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
				  asyncCargaMasiva.procesaArchivoAsync(bytes);
			  }

//            System.out.println("Is Multipart");
//            Iterator<String> iterator = request.getFileNames();
//            MultipartFile file = request.getFile(iterator.next());
//
//            System.out.println("File Length: " + file.getBytes().length);
//            System.out.println("File Type: " + file.getContentType());
//
//            String fileName = file.getOriginalFilename();
//
//            System.out.println("File Name: " + fileName);
//
//            asyncCargaMasiva.asyncCargaMasiva(file.getBytes());
//				ServletFileUpload upload = new ServletFileUpload();
//
//				FileItemIterator iterator = upload.getItemIterator(request);
//
//				while(iterator.hasNext()){
//					FileItemStream item = iterator.next();
//					String name = item.getFieldName();
//					InputStream stream = item.openStream();
//					if(!item.isFormField()){
//						String filename = item.getName();
//						OutputStream out = new FileOutputStream(filename);
//						stream.close();
//						out.close();
//					}
//				}
//			XSSFWorkbook workbook = new XSSFWorkbook(fileXlsx.getInputStream());
//			reportesExcelImpl.leerArchivoCargaMasiva(workbook);

        } catch (Exception ne) {

//			String error = new CustomUserException().getMessage();
//			String[] errorMessage = error.split("\\:");

            logger.error("Formato incorrecto", ne);
            redirect.addFlashAttribute("error", "Error al momento de realizar la inserción masiva");

            return "redirect:/cargaMasiva/afiliados";
        }

        redirect.addFlashAttribute("info", "El archivo se está procesando");
        return "redirect:/afiliados/ver";

    }

}
