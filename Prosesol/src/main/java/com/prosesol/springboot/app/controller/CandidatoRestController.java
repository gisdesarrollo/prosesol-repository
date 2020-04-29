package com.prosesol.springboot.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prosesol.springboot.app.entity.Candidato;
import com.prosesol.springboot.app.entity.dao.ICandidatoDao;
import com.prosesol.springboot.app.service.ICandidatoService;


@RestController
public class CandidatoRestController {
	
		protected static final Log logger=LogFactory.getLog(CandidatoRestController.class);
		
		@Autowired
		private ICandidatoDao candidatoDao;
		
		@Autowired
		private ICandidatoService candidatoService;
		
		@Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
	    @RequestMapping(value = "/data/candidatos", method = RequestMethod.GET)
	    public @ResponseBody void getCandidato(DataTablesInput input, HttpServletResponse response) {
			
			String estatus = "";
			
			DataTablesOutput<Candidato> dtCandidato = candidatoDao.findAll(input);
			
			try {
	            List<Candidato> candidatos = dtCandidato.getData();

	            Double saldoAcumulado = new Double(0L);

	            String data = "";
	            int totalCandidatos = candidatos.size();
	            int index = 1;

	            for (Candidato candidato : candidatos) {

	                if(candidato.getEstatus() == 1){
	                    estatus = "Activo";
	                }else if(candidato.getEstatus() == 2){
	                    estatus = "Inactivo";
	                }else if(candidato.getEstatus() == 3){
	                    estatus = "Candidato";
	                }

	                data += "{" +
	                        "\"nombre\" :" + "\"" + candidato.getNombre() + "\", " +
	                        "\"apellidoPaterno\" :" + "\"" + candidato.getApellidoPaterno() + "\", " +
	                        "\"apellidoMaterno\" :" + "\"" + candidato.getApellidoMaterno() + "\", " +
	                        "\"email\" :" + "\"" + (candidato.getEmail()!=null ? candidato.getEmail() : "" ) + "\", " +
	                        "\"telefonoFijo\" :" + "\"" + (candidato.getTelefonoFijo()!=null ? candidato.getTelefonoFijo() : "" ) + "\", " +
	                        "\"telefonoMovil\" :" + "\"" + (candidato.getTelefonoMovil()!=null ? candidato.getTelefonoMovil() : "" ) + "\", " +
	                        "\"rfc\" :" + "\"" + candidato.getRfc() + "\", " +
	                        "\"id\" :" + "\"" + candidato.getId() + "\"" +
	                        "}";
	                if (index < totalCandidatos) {
	                    data += ",";
	                }

	                logger.info(data);
	                index++;
	            }
	            
	            if(data == ""){

	                int nIndex = 1;
	                String[] nombreCompleto = input.getSearch().getValue().split(" ");
	                if(nombreCompleto.length == 2){
	                    List<Candidato> lCandidatos = candidatoService.getCandidatoBySearchNombreCompleto(nombreCompleto[0],
	                            nombreCompleto[1], "");
	                    int totalCandidatosBusqueda = lCandidatos.size();
	                    if(lCandidatos != null && lCandidatos.size() > 0) {
	                        for (Candidato candidato : lCandidatos) {

	                            if(candidato.getEstatus() == 1){
	                                estatus = "Activo";
	                            }else if(candidato.getEstatus() == 2){
	                                estatus = "Inactivo";
	                            }else if(candidato.getEstatus() == 3){
	                                estatus = "Candidato";
	                            }

	                            data += "{" +
	                                    "\"nombre\" :" + "\"" + candidato.getNombre() + "\", " +
	                                    "\"apellidoPaterno\" :" + "\"" + candidato.getApellidoPaterno() + "\", " +
	                                    "\"apellidoMaterno\" :" + "\"" + candidato.getApellidoMaterno() + "\", " +
	                                    "\"email\" :" + "\"" + (candidato.getEmail()!=null ? candidato.getEmail() : "" ) + "\", " +
	                                    "\"telefonoFijo\" :" + "\"" + (candidato.getTelefonoFijo()!=null ? candidato.getTelefonoFijo() : "" ) + "\", " +
	        	                        "\"telefonoMovil\" :" + "\"" + (candidato.getTelefonoMovil()!=null ? candidato.getTelefonoMovil() : "" ) + "\", " +
	                                    "\"rfc\" :" + "\"" + candidato.getRfc() + "\", " +
	                                    "\"id\" :" + "\"" + candidato.getId() + "\"" +
	                                    "}";
	                            if (nIndex < totalCandidatosBusqueda) {
	                                data += ",";
	                            }

	                            logger.info(data);
	                            nIndex++;
	                        }
	                    }
	                }
	                if(nombreCompleto.length == 3){
	                    List<Candidato> lCandidatos = candidatoService.getCandidatoBySearchNombreCompleto(
	                            nombreCompleto[0], nombreCompleto[1], nombreCompleto[2]);
	                    int totalCandidatosBusqueda = lCandidatos.size();
	                    if(lCandidatos != null && lCandidatos.size() > 0) {
	                        for (Candidato candidato : lCandidatos) {

	                            if(candidato.getEstatus() == 1){
	                                estatus = "Activo";
	                            }else if(candidato.getEstatus() == 2){
	                                estatus = "Inactivo";
	                            }else if(candidato.getEstatus() == 3){
	                                estatus = "Candidato";
	                            }

	                            data += "{" +
	                                    "\"nombre\" :" + "\"" + candidato.getNombre() + "\", " +
	                                    "\"apellidoPaterno\" :" + "\"" + candidato.getApellidoPaterno() + "\", " +
	                                    "\"apellidoMaterno\" :" + "\"" + candidato.getApellidoMaterno() + "\", " +
	                                    "\"email\" :" + "\"" + (candidato.getEmail()!=null ? candidato.getEmail() : "" ) + "\", " +
	                                    "\"telefonoFijo\" :" + "\"" + (candidato.getTelefonoFijo()!=null ? candidato.getTelefonoFijo() : "" ) + "\", " +
	        	                        "\"telefonoMovil\" :" + "\"" + (candidato.getTelefonoMovil()!=null ? candidato.getTelefonoMovil() : "" ) + "\", " +
	                                    "\"rfc\" :" + "\"" + candidato.getRfc() + "\", " +
	                                    "\"id\" :" + "\"" + candidato.getId() + "\"" +
	                                    "}";
	                            if (nIndex < totalCandidatosBusqueda) {
	                                data += ",";
	                            }

	                            logger.info(data);
	                            nIndex++;
	                        }
	                    }else{
	                        lCandidatos = candidatoService.getCandidatoBySearchNombreCompleto(
	                                nombreCompleto[0] + ' ' + nombreCompleto[1], nombreCompleto[2], "%%");
	                        if(lCandidatos != null && lCandidatos.size() > 0) {
	                            for (Candidato candidato : lCandidatos) {

	                                if(candidato.getEstatus() == 1){
	                                    estatus = "Activo";
	                                }else if(candidato.getEstatus() == 2){
	                                    estatus = "Inactivo";
	                                }else if(candidato.getEstatus() == 3){
	                                    estatus = "Candidato";
	                                }

	                                data += "{" +
	                                        "\"nombre\" :" + "\"" + candidato.getNombre() + "\", " +
	                                        "\"apellidoPaterno\" :" + "\"" + candidato.getApellidoPaterno() + "\", " +
	                                        "\"apellidoMaterno\" :" + "\"" + candidato.getApellidoMaterno() + "\", " +
	                                        "\"email\" :" + "\"" + (candidato.getEmail()!=null ? candidato.getEmail() : "" ) + "\", " +
	                                        "\"telefonoFijo\" :" + "\"" + (candidato.getTelefonoFijo()!=null ? candidato.getTelefonoFijo() : "" ) + "\", " +
	            	                        "\"telefonoMovil\" :" + "\"" + (candidato.getTelefonoMovil()!=null ? candidato.getTelefonoMovil() : "" ) + "\", " +
	                                        "\"rfc\" :" + "\"" + candidato.getRfc() + "\", " +
	                                        "\"id\" :" + "\"" + candidato.getId() + "\"" +
	                                        "}";
	                                if (nIndex < totalCandidatosBusqueda) {
	                                    data += ",";
	                                }

	                                logger.info(data);
	                                nIndex++;
	                            }
	                        }
	                    }
	                }
	                if(nombreCompleto.length == 4){
	                    List<Candidato> lCandidatos = candidatoService.getCandidatoBySearchNombreCompleto(
	                            nombreCompleto[0] + ' ' + nombreCompleto[1], nombreCompleto[2], nombreCompleto[3]);
	                    int totalCandidatosBusqueda = lCandidatos.size();
	                    if(lCandidatos != null && lCandidatos.size() > 0) {
	                        for (Candidato candidato : lCandidatos) {

	                            if(candidato.getEstatus() == 1){
	                                estatus = "Activo";
	                            }else if(candidato.getEstatus() == 2){
	                                estatus = "Inactivo";
	                            }else if(candidato.getEstatus() == 3){
	                                estatus = "Candidato";
	                            }

	                            data += "{" +
	                                    "\"nombre\" :" + "\"" + candidato.getNombre() + "\", " +
	                                    "\"apellidoPaterno\" :" + "\"" + candidato.getApellidoPaterno() + "\", " +
	                                    "\"apellidoMaterno\" :" + "\"" + candidato.getApellidoMaterno() + "\", " +
	                                    "\"email\" :" + "\"" + (candidato.getEmail()!=null ? candidato.getEmail() : "" ) + "\", " +
	                                    "\"telefonoFijo\" :" + "\"" + (candidato.getTelefonoFijo()!=null ? candidato.getTelefonoFijo() : "" ) + "\", " +
	        	                        "\"telefonoMovil\" :" + "\"" + (candidato.getTelefonoMovil()!=null ? candidato.getTelefonoMovil() : "" ) + "\", " +
	                                    "\"rfc\" :" + "\"" + candidato.getRfc() + "\", " +
	                                    "\"id\" :" + "\"" + candidato.getId() + "\"" +
	                                    "}";
	                            if (nIndex < totalCandidatosBusqueda) {
	                                data += ",";
	                            }

	                            logger.info(data);
	                            nIndex++;
	                        }
	                    }
	                }
	            }

	            String json = "{" +
	                    "\"recordsTotal\" : " + dtCandidato.getRecordsTotal() + "," +
	                    "\"recordsFiltered\" : " + dtCandidato.getRecordsFiltered() + "," +
	                    "\"data\" : [" + data + "]" +
	                    "}";

	            response.setStatus(200);
	            response.setContentType("application/x-json;charset=UTF-8");
	            response.getWriter().write(json);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
}
