package com.prosesol.springboot.app.controller;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.prosesol.springboot.app.entity.Beneficiario;

@Controller
@SessionAttributes("beneficiario")
public class BeneficiarioController {

	protected Log logger = LogFactory.getLog(this.getClass());

	@RequestMapping(value = "/crearBeneficiario")
	public String crear(Map<String, Object> model) {

		Beneficiario beneficiario = new Beneficiario();

		System.out.println("Entrar al método crear afiliado desde el button Agregar Afiliado");
		
		model.put("beneficiario", beneficiario);
		model.put("titulo", "Crear beneficiario");

		return "/catalogos/beneficiarios/crear";
	}

	
}
