package com.prosesol.springboot.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.dao.IAfiliadoDao;
import com.prosesol.springboot.app.service.IAfiliadoService;

@RestController
public class AfiliadoRestController {

	@Autowired
	public IAfiliadoDao afiliadoDao;
	
	@Autowired
	public IAfiliadoService afiliadoService;
	
	@RequestMapping(value = "/data/afiliados", method = RequestMethod.GET)
	public @ResponseBody void getAfiliado(DataTablesInput input, HttpServletResponse response){
		
		System.out.println(input.getLength());
		DataTablesOutput<Afiliado> dtoAfiliado = afiliadoDao.findAll(input);
//		List<Afiliado> lengthDB = afiliadoService.findAll();
		
		try {
			List<Afiliado> afiliados = dtoAfiliado.getData();
			
			String data = "";
			int totalAfiliados = afiliados.size();
			int index = 1;
		
				
			for(Afiliado afiliado : afiliados) {
				data += "{" +
						"\"nombre\" :" + "\"" + afiliado.getNombre() + "\", " +
						"\"apellidoPaterno\" :" + "\"" + afiliado.getApellidoPaterno() + "\", " +
						"\"apellidoMaterno\" :" + "\"" + afiliado.getApellidoMaterno() + "\", " +
						"\"clave\" :" + "\"" +afiliado.getClave() + "\", " +
						"\"saldoAcumulado\" : " + "\"" + afiliado.getSaldoAcumulado() + "\"" +
						"}";
				if(index < totalAfiliados) {
					data += ",";
				}
				
				index++;
			}
			
			String json = "{" +
						  "\"recordsTotal\" : " + dtoAfiliado.getRecordsTotal() + "," +
						  "\"recordsFiltered\" : " + dtoAfiliado.getRecordsFiltered() + "," +
						  "\"data\" : [" + data + "]" +
						  "}";
			
			response.setStatus(200);
			response.getWriter().write(json);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	@JsonView(DataTablesOutput.View.class)
//	@RequestMapping(value = "/data/afiliados", method = RequestMethod.GET)
//	public DataTablesOutput<Afiliado> getAfiliado(DataTablesInput input){
//		System.out.println(input.getLength());
//		return afiliadoDao.findAll(input);
//	}
}