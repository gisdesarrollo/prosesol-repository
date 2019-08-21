package com.prosesol.springboot.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.prosesol.springboot.app.service.IAfiliadoService;
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

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.dao.IAfiliadoDao;

@RestController
public class AfiliadoRestController {

    protected static final Log logger = LogFactory.getLog(AfiliadoRestController.class);

    @Autowired
    private IAfiliadoDao afiliadoDao;

    @Autowired
    private IAfiliadoService afiliadoService;

    @Secured({"ROLE_ADMINISTRADOR", "ROLE_USUARIO"})
    @RequestMapping(value = "/data/afiliados", method = RequestMethod.GET)
    public @ResponseBody
    void getAfiliado(DataTablesInput input, HttpServletResponse response) {

        System.out.println(input.getColumns());
        DataTablesOutput<Afiliado> dtoAfiliado = afiliadoDao.findAll(input);

        try {
            List<Afiliado> afiliados = dtoAfiliado.getData();

            Double saldoAcumulado = new Double(0L);

            String data = "";
            int totalAfiliados = afiliados.size();
            int index = 1;

            for (Afiliado afiliado : afiliados) {
                data += "{" +
                        "\"nombre\" :" + "\"" + afiliado.getNombre() + "\", " +
                        "\"apellidoPaterno\" :" + "\"" + afiliado.getApellidoPaterno() + "\", " +
                        "\"apellidoMaterno\" :" + "\"" + afiliado.getApellidoMaterno() + "\", " +
                        "\"clave\" :" + "\"" + afiliado.getClave() + "\", " +
                        "\"saldoAcumulado\" : " + "\"" + (afiliado.getSaldoAcumulado() != null ? afiliado.getSaldoAcumulado() : saldoAcumulado) + "\", " +
                        "\"isBeneficiario\" : " + "\"" + (afiliado.getIsBeneficiario().equals(true) ? "Beneficiario" : "Titular") + "\", " +
                        "\"estatus\" : " + "\"" + (afiliado.getEstatus() == 1 ? "Activo" : "Inactivo") + "\", " +
                        "\"servicio\" : {" + "\"nombre\" : " + "\"" + afiliado.getServicio().getNombre() + "\"}," +
                        "\"id\" :" + "\"" + afiliado.getId() + "\"" +
                        "}";
                if (index < totalAfiliados) {
                    data += ",";
                }

                logger.info(data);
                index++;
            }

            if(data == ""){
                String[] nombreCompleto = input.getSearch().getValue().split(" ");
                if(nombreCompleto.length == 3){
                    Long claveAfiliado = afiliadoService.getIdAfiliadoByNombreCompleto(nombreCompleto[0], nombreCompleto[1], nombreCompleto[2]);
                    if(claveAfiliado != null && claveAfiliado > 0){
                        Afiliado afiliado = afiliadoService.findById(claveAfiliado);
                        data += "{" +
                                "\"nombre\" :" + "\"" + afiliado.getNombre() + "\", " +
                                "\"apellidoPaterno\" :" + "\"" + afiliado.getApellidoPaterno() + "\", " +
                                "\"apellidoMaterno\" :" + "\"" + afiliado.getApellidoMaterno() + "\", " +
                                "\"clave\" :" + "\"" + afiliado.getClave() + "\", " +
                                "\"saldoAcumulado\" : " + "\"" + (afiliado.getSaldoAcumulado() != null ? afiliado.getSaldoAcumulado() : saldoAcumulado) + "\", " +
                                "\"isBeneficiario\" : " + "\"" + (afiliado.getIsBeneficiario().equals(true) ? "Beneficiario" : "Titular") + "\", " +
                                "\"estatus\" : " + "\"" + (afiliado.getEstatus() == 1 ? "Activo" : "Inactivo") + "\", " +
                                "\"servicio\" : {" + "\"nombre\" : " + "\"" + afiliado.getServicio().getNombre() + "\"}," +
                                "\"id\" :" + "\"" + afiliado.getId() + "\"" +
                                "}";
                    }
                    logger.info(data);
                }
                if(nombreCompleto.length == 4){
                    Long claveAfiliado = afiliadoService.getIdAfiliadoByNombreCompleto(nombreCompleto[0] + " " + nombreCompleto[1], nombreCompleto[2], nombreCompleto[3]);
                    if(claveAfiliado != null && claveAfiliado > 0){
                        Afiliado afiliado = afiliadoService.findById(claveAfiliado);
                        data += "{" +
                                "\"nombre\" :" + "\"" + afiliado.getNombre() + "\", " +
                                "\"apellidoPaterno\" :" + "\"" + afiliado.getApellidoPaterno() + "\", " +
                                "\"apellidoMaterno\" :" + "\"" + afiliado.getApellidoMaterno() + "\", " +
                                "\"clave\" :" + "\"" + afiliado.getClave() + "\", " +
                                "\"saldoAcumulado\" : " + "\"" + (afiliado.getSaldoAcumulado() != null ? afiliado.getSaldoAcumulado() : saldoAcumulado) + "\", " +
                                "\"isBeneficiario\" : " + "\"" + (afiliado.getIsBeneficiario().equals(true) ? "Beneficiario" : "Titular") + "\", " +
                                "\"estatus\" : " + "\"" + (afiliado.getEstatus() == 1 ? "Activo" : "Inactivo") + "\", " +
                                "\"servicio\" : {" + "\"nombre\" : " + "\"" + afiliado.getServicio().getNombre() + "\"}," +
                                "\"id\" :" + "\"" + afiliado.getId() + "\"" +
                                "}";
                    }
                    logger.info(data);
                }
            }

            String json = "{" +
                    "\"recordsTotal\" : " + dtoAfiliado.getRecordsTotal() + "," +
                    "\"recordsFiltered\" : " + dtoAfiliado.getRecordsFiltered() + "," +
                    "\"data\" : [" + data + "]" +
                    "}";

            response.setStatus(200);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarAfiliadorByNombre(String value) {



    }

}