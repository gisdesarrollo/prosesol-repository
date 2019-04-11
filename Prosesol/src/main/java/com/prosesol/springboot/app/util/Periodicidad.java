package com.prosesol.springboot.app.util;

import java.util.HashMap;
import java.util.Map;

public class Periodicidad {

	private static Map<Integer, String> periodicidades;
	
	static {
		periodicidades = new HashMap<Integer, String>();
		periodicidades.put(1, "Evento");
		periodicidades.put(2, "Diario");
		periodicidades.put(3, "Semanal");
		periodicidades.put(4, "Mensual");
		periodicidades.put(5, "Bimestral");
		periodicidades.put(6, "Trimestral");
		periodicidades.put(7, "Semestral");
		periodicidades.put(8, "Anual");
	}
	
}
