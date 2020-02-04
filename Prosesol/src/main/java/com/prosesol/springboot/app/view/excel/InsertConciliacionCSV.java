package com.prosesol.springboot.app.view.excel;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Pago;
import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.exception.CustomExcelException;
import com.prosesol.springboot.app.repository.AfiliadoRepository;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.service.IPagoService;
import com.prosesol.springboot.app.service.IPeriodicidadService;
import com.prosesol.springboot.app.service.IServicioService;
import com.prosesol.springboot.app.util.CalcularFecha;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class InsertConciliacionCSV {

	protected final Log LOG = LogFactory.getLog(InsertConciliacionCSV.class);

	@Autowired
	private IAfiliadoService afiliadoService;

	@Autowired
	private IPagoService pagoService;

	@Autowired
	private AfiliadoRepository afiliadoRepository;

	private Collator collator = Collator.getInstance(new Locale("es"));
	private boolean isValidPagos;
	private boolean isValid;

	String log = "";
	
	
	public String evaluarDatosList(boolean isVigor, boolean isConciliacion ,Integer counterLinea, Map<Integer, String> campos) {
		 
		Pago pagos = new Pago();
		Afiliado afiliado = new Afiliado();
	 	isValidPagos = true;

		for (Map.Entry<Integer, String> campo : campos.entrySet()) {
			try {

				switch (campo.getKey()) {
				case 0:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El rfc no puede quedar vacío");
						log = counterLinea + " - " + "El rfc no puede quedar vacío";
						isValidPagos = false;
						
					}else if(campo.getValue().length() != 13){
						LOG.info(counterLinea + " - " + "El rfc no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El rfc no cuenta con la longitud correcta";
						isValidPagos = false;
						
					} else {
						afiliado = afiliadoService.getAfiliadoByRfc(campo.getValue());
						if (afiliado == null) {
							LOG.info(counterLinea + " - " + "El Rfc del afiliado no se encuentra registrado");
							log = counterLinea + " - " + "El Rfc del afiliado no se encuentra registrado";
							isValidPagos = false;

						}
					}
					break;
				case 1:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El monto no puede quedar vacío");
						log = counterLinea + " - " + "El monto no puede quedar vacío";
						isValidPagos = false;
					} else {
						pagos.setMonto(Double.parseDouble(campo.getValue()));
						LOG.info(counterLinea + " - " + "Monto: " + pagos.getMonto());
					}
					break;
				case 2:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "La referencia bancaria no puede quedar vacío");
						log = counterLinea + " - " + "La referencia bancaria no puede quedar vacío";
						isValidPagos = false;
					} else {
						pagos.setReferenciaBancaria(campo.getValue());
						LOG.info(counterLinea + " - " + "Referencia Bancaria: " + pagos.getReferenciaBancaria());

					}
					break;
				case 3:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "La fecha de pago no debe quedar vacío");
						log = counterLinea + " - " + "La fecha de pago no debe quedar vacío";
						isValidPagos = false;
					} else {
						isValid = isValidFormat("dd/MM/yyyy", campo.getValue());
		                
						if (isValid) {
							pagos.setFechaPago(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
							LOG.info(counterLinea + " - " + "Fecha de Pago: " + pagos.getFechaPago());
						} else {
							log = counterLinea + " - " + "Formato de fecha incorrecto, formato correcto dd/mm/yyyy";
							LOG.info(counterLinea + " - " + "Formato de fecha incorrecto, formato correcto dd/mm/yyyy");
							isValidPagos = false;
						}
					}

					break;
				}

				if (isValidPagos) {
					continue;
				} else {
					break;
				}

			} catch (IllegalArgumentException e) {
				LOG.error(counterLinea + " - " + "Error al momento de leer el archivo", e);
				throw new CustomExcelException(
						"Error en la línea " + counterLinea + ": Verifique que los campos " + "sean correctos");

			} catch (ParseException pe) {
				LOG.error(counterLinea + " - " + "Error al momento de convertir la fecha: ", pe);
				log = counterLinea + " - " + "Error al momento de convertir la fecha: " + pe.getMessage();

			} catch (Exception e) {
				LOG.error(counterLinea + " - " + "Error al momento de leer el archivo", e);
				log = counterLinea + " - " + "Error al momento de leer el archivo" + e.getMessage();

			}
		}

		if (!isVigor && isConciliacion) {
			log = insertPagosAfiliado(isValidPagos, pagos, counterLinea, afiliado);
		}

		return log;
	}

	/**
	 * Método que inserta los pagos del afiliado
	 * 
	 * @param isValidPagos
	 * @param pagos
	 * @param counterLinea
	 * @return
	 */

	private String insertPagosAfiliado(boolean isValidPagos, Pago pagos, Integer counterLinea,
									   Afiliado afiliado) {

		try {

			if (isValidPagos) {

				pagos.setEstatus("completed");
				pagos.setTipoTransaccion("Conciliación");
				pagoService.save(pagos);

				afiliadoRepository.insertRelAfiliadosPagos(afiliado, pagos.getId());
				
				if (!isValidPagos) {
					LOG.info(counterLinea + " - "
							+ "El pago no se ha registrado, verifique los datos que sean correctos");
				} else {
					LOG.info(counterLinea + " - " + "El pago se ha insertado correctamente");
					log = counterLinea + " - " + "El pago se ha insertado correctamente";
				}

			} else {
				LOG.info(counterLinea + " - " + "El pago no se ha registrado, verifique los datos que sean correctos");
			}
		} catch (Exception e) {
			LOG.info(counterLinea + " - " + "Error al momento de guardar los pagos", e);
			log = counterLinea + " - " + "Error al momento de guardar los pagos: " + e.getMessage();
		}

		return log;
	}

	/**
	 * Evalúa si el formato de fecha es el correcto
	 * 
	 * @param format
	 * @param value
	 * @return
	 */
	private static boolean isValidFormat(String format, String value) {

		boolean isValid = false;
		Date date = null;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			date = dateFormat.parse(value);

			if (value.equals(dateFormat.format(date))) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		return isValid;
	}


	/**
	 * Verifica los campos de tipo string que no sean númericos
	 * 
	 * @param cadena
	 * @return
	 */
	private boolean isInteger(String cadena) {
		if (NumberUtils.isCreatable(cadena)) {
			return true;
		} else {
			return false;
		}
	}
}
