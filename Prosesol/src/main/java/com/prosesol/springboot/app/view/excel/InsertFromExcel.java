package com.prosesol.springboot.app.view.excel;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

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

	public void insertToDBFromExcel(Map<Integer, String> listValues) throws ParseException {

		LOGGER.info("Inicia la inserción de datos de carga masiva");
		
		Collator collator = Collator.getInstance(new Locale("es"));

		Afiliado afiliado = new Afiliado();
		Periodicidad periodicidad = new Periodicidad();

		Integer numeroDependientes = null;
		Long telefonoFijo = null;
		Long telefonoMovil = null;
		String comentarios = null;
		String rfcAfiliado = null;
		String periodoString = null;
		Long idAfiliado = null;

		List<Periodicidad> periodos = periodicidadService.findAll();
		List<Cuenta> cuentas = cuentaService.findAll();
		List<Promotor> promotores = promotorService.findAll();
		List<Servicio> servicios = servicioService.findAll();

		int rowKey = 0;

		List<String> listaAfiliado = new ArrayList<String>();

		for (Entry<Integer, String> values : listValues.entrySet()) {

			listaAfiliado.add(values.getValue());
			System.out.println("Clave: " + values.getKey() + " Valor: " + values.getValue());

			if (values.getKey() == 6) {
				numeroDependientes = Integer.parseInt(values.getValue());
			}			
			if (values.getKey() == 13) {
				telefonoFijo = Long.parseLong(values.getValue());
			}
			if (values.getKey() == 14) {
				telefonoMovil = Long.parseLong(values.getValue());
			}
			if (values.getKey() == 24) {
				periodoString = values.getValue();
			}
			if (values.getKey() == 25) {
				comentarios = values.getValue();
			}
			if (values.getKey() == 27) {
				rfcAfiliado = values.getValue();
			}
		}

		System.out.println(rfcAfiliado);
		System.out.println(comentarios);
		
		afiliado.setNombre(listaAfiliado.get(rowKey++));
		afiliado.setApellidoPaterno(listaAfiliado.get(rowKey++));
		afiliado.setApellidoMaterno(listaAfiliado.get(rowKey++));
		afiliado.setFechaNacimiento(new SimpleDateFormat("yyyy/MM/dd").parse(listaAfiliado.get(rowKey++)));
		afiliado.setLugarNacimiento(listaAfiliado.get(rowKey++));
		afiliado.setEstadoCivil(listaAfiliado.get(rowKey++));

		if (numeroDependientes != null) {
			afiliado.setNumeroDependientes(Integer.parseInt(listaAfiliado.get(rowKey++)));
		}

		afiliado.setOcupacion(listaAfiliado.get(rowKey++));
		afiliado.setSexo(listaAfiliado.get(rowKey++));
		afiliado.setPais(listaAfiliado.get(rowKey++));
		afiliado.setCurp(listaAfiliado.get(rowKey++));

		Long nss = Long.parseLong(listaAfiliado.get(rowKey++));
		afiliado.setNss(nss);

		String rfc = listaAfiliado.get(rowKey++);
		afiliado.setRfc(rfc);

		if (telefonoFijo != null) {
			afiliado.setTelefonoFijo(Long.parseLong(listaAfiliado.get(rowKey++)));
		}

		if (telefonoMovil != null) {
			afiliado.setTelefonoMovil(Long.parseLong(listaAfiliado.get(rowKey++)));
		}

		afiliado.setEmail(listaAfiliado.get(rowKey++));
		afiliado.setDireccion(listaAfiliado.get(rowKey++));
		afiliado.setMunicipio(listaAfiliado.get(rowKey++));

		Long codigoPostal = Long.parseLong(listaAfiliado.get(rowKey++));
		afiliado.setCodigoPostal(codigoPostal);

		afiliado.setEntidadFederativa(listaAfiliado.get(rowKey++));

		String infonavit = listaAfiliado.get(rowKey++);

		if (infonavit.equals("No")) {			
			afiliado.setNumeroInfonavit(null);			
		} else {
			afiliado.setNumeroInfonavit(Long.parseLong(listaAfiliado.get(rowKey++)));
		}

		afiliado.setFechaAfiliacion(new SimpleDateFormat("yyyy/MM/dd").parse(listaAfiliado.get(rowKey++)));

		String servicioString = listaAfiliado.get(rowKey++);

		for (Servicio servicio : servicios) {
			if (servicioString.equals(servicio.getNombre())) {
				afiliado.setServicio(servicio);
			}
		}

		if(periodoString != null) {
			
			String p = listaAfiliado.get(rowKey++);
			
			for (Periodicidad periodo : periodos) {
				if (p.equals(periodo.getNombre())) {
					periodicidad = periodo;
					afiliado.setPeriodicidad(periodicidad);
				}
			}	
		}
		
		
		if (comentarios != null) {
			afiliado.setComentarios(listaAfiliado.get(rowKey++));
		}

		String isBeneficiario = listaAfiliado.get(rowKey++);
		System.out.println(isBeneficiario);

		if (isBeneficiario.equals("No")) {
			afiliado.setIsBeneficiario(false);
		} else {
			afiliado.setIsBeneficiario(true);
		}

		String promotorString = listaAfiliado.get(rowKey++);

		for (Promotor promotor : promotores) {
			if (promotorString.equals(promotor.getNombre())) {
				afiliado.setPromotor(promotor);
			}
		}

		String cuentaString = listaAfiliado.get(rowKey++);

		for (Cuenta cuenta : cuentas) {
			if (cuentaString.equals(cuenta.getRazonSocial())) {
				afiliado.setCuenta(cuenta);
			}
		}

		if (isBeneficiario.equals("No")) {

			Integer corte = Integer.parseInt(listaAfiliado.get(rowKey++));
			Date fechaCorte = calcularFechas.calcularFechas(periodicidad, corte);
			afiliado.setFechaCorte(fechaCorte);
		}else {
			
			idAfiliado = getIdAfiliadoByRfc(rfcAfiliado);
			System.out.println(idAfiliado);
		}

		afiliado.setFechaAlta(new Date());
		afiliado.setEstatus(3);
		afiliado.setClave(getClaveAfiliado());

		afiliadoService.save(afiliado);
		
		collator.setStrength(Collator.PRIMARY);		
		System.out.println(isBeneficiario + (collator.equals(isBeneficiario, "Sí") ? "eq" : "ne") + "Sí");
		
		if (collator.equals(isBeneficiario, "Sí")) {			
			afiliadoService.insertBeneficiarioUsingJpa(afiliado, idAfiliado);		
		}

		LOGGER.info("Termina la inserción de afiliado desde la carga masiva");
	}

	@ModelAttribute("clave")
	private String getClaveAfiliado() {

		String claveAfiliado = "";

		for (int i = 0; i < 10; i++) {
			claveAfiliado += (clave.charAt((int) (Math.random() * clave.length())));
		}

		return claveAfiliado;
	}

	private Long getIdAfiliadoByRfc(String rfc) {		
		return afiliadoService.getIdAfiliadoByRfc(rfc);		
	}
	
}
