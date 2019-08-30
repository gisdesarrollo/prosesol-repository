package com.prosesol.springboot.app.view.excel;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;

import com.prosesol.springboot.app.util.AfiliadoExcelEval;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.exception.CustomUserException;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.CalcularFecha;

@Component
public class InsertFromExcel {

	private static final Log LOGGER = LogFactory.getLog(InsertFromExcel.class);

	@Autowired
	AfiliadoExcelEval afiliadoExcelEval;

	/**
	 * Método que funciona para insertar a la BBDD
	 * @param values
	 * @return
	 */

	public List<String> insertAfiliados(String[] values, final int valoresCapturados){

		List<String> log = new ArrayList<String>();

		Map<Integer, String> campos = new HashMap<Integer, String>();

		int counter = 0;
		int startIndex = 0;
		int endIndex = valoresCapturados;

		System.out.println(valoresCapturados);

		//Evalua los datos que vienen del array para generar un TXT con los errores o inserciones

		while(endIndex <= values.length){

			String[] copy = Arrays.copyOfRange(values, startIndex, endIndex);

			for(int i = 0; i < copy.length; i++){
				campos.put(i, copy[i]);
			}

			afiliadoExcelEval.evaluarDatosList(campos);

			startIndex = endIndex;
			endIndex = endIndex * 2;
		}

		return null;
	}

}