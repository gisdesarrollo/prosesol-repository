package com.prosesol.springboot.app.view.excel;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Cuenta;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.ICuentaService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IPromotorService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.CalcularFecha;

@Component
public class InsertFromExcel {

	private static final Log LOGGER = LogFactory.getLog(InsertFromExcel.class);

	@Value("${app.clave}")
	private String clave;

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private IPeriodicidadService periodicidadService;

	@Autowired
	private ICuentaService cuentaService;

	@Autowired
	private IPromotorService promotorService;

	@Autowired
	private IServicioService servicioService;

	@Autowired
	private CalcularFecha calcularFechas;
	
	private String servicioString;
	private String periodoString;
	private String promotorString;	
	private String cuentaString;
	private String isBeneficiario;
	private String rfcAfiliado;
	
	private int corte;
	private Long idAfiliado;
	
	public void insertToDBFromExcel(Map<Integer, String> listValues) {

		LOGGER.info("Inicia la inserción de datos de carga masiva");	
		
		Collator collator = Collator.getInstance(new Locale("es"));
		
		Afiliado afiliado = new Afiliado();
		Periodicidad periodicidad = new Periodicidad();
		
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(
					(Comparator<Integer>)(o1, o2) -> o1.compareTo(o2)
				);
		treeMap.putAll(listValues);
		
		List<String> afiliadoValues = new ArrayList<String>();
		List<Periodicidad> periodos = periodicidadService.findAll();
		List<Cuenta> cuentas = cuentaService.findAll();
		List<Promotor> promotores = promotorService.findAll();
		List<Servicio> servicios = servicioService.findAll();
		
		for(Entry<Integer, String> values : treeMap.entrySet()) {
			afiliadoValues.add(values.getValue());
			System.out.println("Clave: " + values.getKey() + " Valor: " + values.getValue());
			
			try {
				
				if(values.getKey().equals(0)) {
					afiliado.setNombre(values.getValue());
				}
				if(values.getKey().equals(1)) {
					afiliado.setApellidoPaterno(values.getValue());
				}
				if(values.getKey().equals(2)) {
					afiliado.setApellidoMaterno(values.getValue());
				}
				if(values.getKey().equals(3)) {
					afiliado.setFechaNacimiento(new SimpleDateFormat("yyyy/MM/dd").parse(values.getValue()));
				}
				if(values.getKey().equals(4)) {
					afiliado.setLugarNacimiento(values.getValue());
				}
				if(values.getKey().equals(5)) {
					afiliado.setEstadoCivil(values.getValue());
				}
				if(values.getKey().equals(6)) {
					afiliado.setNumeroDependientes(Integer.parseInt(values.getValue()));
				}
				if(values.getKey().equals(7)) {
					afiliado.setOcupacion(values.getValue());
				}
				if(values.getKey().equals(8)) {
					afiliado.setSexo(values.getValue());
				}
				if(values.getKey().equals(9)) {
					afiliado.setPais(values.getValue());
				}
				if(values.getKey().equals(10)) {
					afiliado.setCurp(values.getValue());
				}
				if(values.getKey().equals(11)) {
					afiliado.setNss(Long.parseLong(values.getValue()));
				}
				if(values.getKey().equals(12)) {
					afiliado.setRfc(values.getValue());
				}
				if(values.getKey().equals(13)) {
					afiliado.setTelefonoFijo(Long.parseLong(values.getValue()));
				}
				if(values.getKey().equals(14)) {
					afiliado.setTelefonoMovil(Long.parseLong(values.getValue()));
				}
				if(values.getKey().equals(15)) {
					afiliado.setEmail(values.getValue());
				}
				if(values.getKey().equals(16)) {
					afiliado.setDireccion(values.getValue());
				}
				if(values.getKey().equals(17)) {
					afiliado.setMunicipio(values.getValue());
				}
				if(values.getKey().equals(18)) {
					afiliado.setCodigoPostal(Long.parseLong(values.getValue()));
				}
				if(values.getKey().equals(19)) {
					afiliado.setEntidadFederativa((values.getValue()));
				}
				if(values.getKey().equals(20)) {
					afiliado.setInfonavit(values.getValue());				
				}
				if(values.getKey().equals(21)) {
					afiliado.setNumeroInfonavit(Long.parseLong(values.getValue()));
				}
				if(values.getKey().equals(22)) {
					afiliado.setFechaAfiliacion(new SimpleDateFormat("yyyy/MM/dd").parse(values.getValue()));
				}
				if(values.getKey().equals(23)) {
					servicioString = values.getValue();
				}
				if(values.getKey().equals(24)) {
					periodoString = values.getValue();
				}
				if(values.getKey().equals(25)) {
					afiliado.setComentarios(values.getValue());
				}
				if(values.getKey().equals(26)) {
					isBeneficiario = values.getValue();
				}
				if(values.getKey().equals(27)) {
					rfcAfiliado = values.getValue();
				}
				if(values.getKey().equals(28)) {
					promotorString = values.getValue();
				}
				if(values.getKey().equals(29)) {
					cuentaString = values.getValue();
				}
				if(values.getKey().equals(30)) {
					corte = Integer.parseInt(values.getValue());
				}
				
				if(servicioString != null) {
					for(Servicio servicio : servicios) {
						if(servicioString.equals(servicio.getNombre())) {
							afiliado.setServicio(servicio);
						}
					}
				}
				
				if(promotorString != null) {
					for(Promotor promotor : promotores) {
						if(promotorString.equals(promotor.getNombre())) {
							afiliado.setPromotor(promotor);
						}
					}
				}
				
				if(periodoString != null) {
					for(Periodicidad periodo : periodos) {
						if(periodoString.equals(periodo.getNombre())) {
							periodicidad = periodo;
							afiliado.setPeriodicidad(periodo);
						}
					}
				}
				
				if(cuentaString != null) {
					for(Cuenta cuenta : cuentas) {
						if(cuentaString.equals(cuenta.getRazonSocial())) {
							afiliado.setCuenta(cuenta);
						}
					}
				}						
				
			}catch(Exception e) {
				LOGGER.error("Error al momento de leer el archivo");
			}
				
		}
		
		collator.setStrength(Collator.PRIMARY);
		if(collator.equals(isBeneficiario, "Sí")) {
			afiliado.setIsBeneficiario(true);
			
			idAfiliado = afiliadoService.getIdAfiliadoByRfc(rfcAfiliado);					
		}else if(isBeneficiario.equals("No")) {
			
			afiliado.setIsBeneficiario(false);
			
			Date fechaCorte = calcularFechas.calcularFechas(periodicidad, corte);	
			afiliado.setFechaCorte(fechaCorte);
		}
		
		afiliado.setFechaAlta(new Date());
		afiliado.setEstatus(3);
		afiliado.setClave(getClaveAfiliado());
		
		afiliadoService.save(afiliado);
		
		if (collator.equals(isBeneficiario, "Sí")) {
			afiliadoService.insertBeneficiarioUsingJpa(afiliado, idAfiliado);
		}
		
		LOGGER.info("Termina la inserción de afiliado desde la carga masiva");
	}

	private String getClaveAfiliado() {

		String claveAfiliado = "";

		for (int i = 0; i < 10; i++) {
			claveAfiliado += (clave.charAt((int) (Math.random() * clave.length())));
		}

		return claveAfiliado;
	}

}