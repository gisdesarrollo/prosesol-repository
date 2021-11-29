package com.prosesol.springboot.app.view.excel;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import com.prosesol.springboot.app.exception.CustomExcelException;
import com.prosesol.springboot.app.service.*;
import com.prosesol.springboot.app.util.CalcularFecha;
import com.prosesol.springboot.app.util.GenerarClave;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InsertCargaMasivaCSV {

	protected final Log LOG = LogFactory.getLog(InsertCargaMasivaCSV.class);

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

	@Autowired
	private GenerarClave generarClave;
	
	@Autowired
	private IRelAfiliadoMoneygramService relAfiMoneygramService;
	
	@Autowired
	private IEmpresaService empresaService;
	
	@Autowired
	private IParametroService parametroService;

	private int corte;
	private Afiliado titular;
	private String isBeneficiario;
	private String rfcAfiliado;
	private Rfc rfc;
	private Date fechaCorte;
	private Integer diaCorte;
	private String dia;
	private DateFormat formatoFecha;
	private final Collator collator = Collator.getInstance(new Locale("es"));
	private DateFormat formatoMesYear;
	private String fechaAfiliacion;
	private String fechaHoy;
	private Date mYA;
	private Date mYH;

	private boolean isValidAfiliado;
	private boolean isValid;
	private boolean isInteger;
	private boolean hasApellidoPaterno;
	String log = "";
	protected final long ID_MONEYGRAM = 1L;
	protected final int PADDING_SIZE = 10;
	
	public String evaluarDatosList(boolean isVigor, boolean isConciliacion, Integer counterLinea,
			Map<Integer, String> campos, Long idCuentaComercial) {

		Afiliado afiliado = new Afiliado();

		isValidAfiliado = true;
		hasApellidoPaterno = true;

		rfc = null;

		if (campos.size() < 26) {
			isBeneficiario = "No";
		}

		for (Map.Entry<Integer, String> campo : campos.entrySet()) {
			try {

				switch (campo.getKey()) {
				case 0:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El nombre no puede quedar vacío");
						log = counterLinea + " - " + "El nombre no puede quedar vacío";
						isValidAfiliado = false;
					} else {
						afiliado.setNombre(campo.getValue());
						isInteger = isInteger(afiliado.getNombre());
						if (!isInteger) {
							isValid = containsVocal(afiliado.getNombre());
							if (isValid) {
								LOG.info(counterLinea + " - " + "Nombre: " + afiliado.getNombre());
							} else {
								log = counterLinea + " - " + "El nombre no contiene una vocal";
								LOG.info(counterLinea + " - " + "El nombre no contiene una vocal");
								isValidAfiliado = false;
							}

						} else {
							log = counterLinea + " - " + "El nombre no puede contener valores númericos";
							LOG.info(counterLinea + " - " + "El nombre no puede contener valores númericos");
							isValidAfiliado = false;
						}

					}
					break;
				case 1:
					afiliado.setApellidoPaterno(campo.getValue());
					isInteger = isInteger(afiliado.getApellidoPaterno());

					if (!isInteger) {
						isValid = containsVocal(afiliado.getApellidoPaterno());
						if (isValid) {
							LOG.info(counterLinea + " - " + "Apellido Paterno: " + afiliado.getApellidoPaterno());
						} else {
							log = counterLinea + " - " + "El Apellido Paterno no contiene una vocal";
							LOG.info(counterLinea + " - " + "El Apellido Paterno no contiene una vocal");
							isValidAfiliado = false;
						}
					} else {
						log = counterLinea + " - " + "El Apellido Paterno no puede contener valores númericos";
						LOG.info(counterLinea + " - " + "El Apellido Paterno no puede contener valores númericos");
						isValidAfiliado = false;
					}
					break;
				case 2:
					afiliado.setApellidoMaterno(campo.getValue());
					isInteger = isInteger(afiliado.getApellidoMaterno());
					if (!isInteger) {
						isValid = containsVocal(afiliado.getApellidoMaterno());
						if (isValid) {
							LOG.info(counterLinea + " - " + "Apellido Materno: " + afiliado.getApellidoMaterno());
						} else {
							log = counterLinea + " - " + "El Apellido Materno no contiene una vocal";
							LOG.info(counterLinea + " - " + "El Apellido Materno no contiene una vocal");
							isValidAfiliado = false;
						}
					} else {
						LOG.info(counterLinea + " - " + "El Apellido Materno no puede contener valores númericos");
						log = counterLinea + " - " + "El Apellido Materno no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 3:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "La fecha de nacimiento no debe quedar vacío");
						log = counterLinea + " - " + "La fecha de nacimiento no debe quedar vacío";
						isValidAfiliado = false;
					} else {
						isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

						if (isValid) {
							afiliado.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
							LOG.info(counterLinea + " - " + "Fecha de Nacimiento: " + afiliado.getFechaNacimiento());
						} else {
							log = counterLinea + " - " + "Formato de fecha incorrecto para la fecha de nacimiento, "
									+ "formato correcto dd/mm/yyyy";
							LOG.info(counterLinea + " - " + "Formato de fecha incorrecto para la fecha de nacimiento, "
									+ "formato correcto dd/mm/yyyy");
							isValidAfiliado = false;
						}
					}
					break;
				case 4:
					afiliado.setLugarNacimiento(campo.getValue());
					isInteger = isInteger(afiliado.getLugarNacimiento());
					if (!isInteger) {
						LOG.info(counterLinea + " - " + "Lugar de Nacimiento: " + afiliado.getLugarNacimiento());
					} else {
						LOG.info(counterLinea + " - " + "El Lugar de Nacimiento no puede contener valores númericos");
						log = counterLinea + " - " + "El Lugar de Nacimiento no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 5:
					afiliado.setEstadoCivil(campo.getValue());
					isInteger = isInteger(afiliado.getEstadoCivil());
					if (!isInteger) {
						LOG.info(counterLinea + " - " + "Estado Civil: " + afiliado.getEstadoCivil());
					} else {
						LOG.info(counterLinea + " - " + "El Estado Civil no puede contener valores númericos");
						log = counterLinea + " - " + "El Estado Civil no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 6:
					if (campo.getValue().length() == 0) {
						afiliado.setNumeroDependientes(null);
						LOG.info(counterLinea + " - " + "Número de Dependientes: " + afiliado.getNumeroDependientes());
					} else {
						afiliado.setNumeroDependientes(Integer.parseInt(campo.getValue()));
						LOG.info(counterLinea + " - " + "Número de Dependientes: " + afiliado.getNumeroDependientes());
					}
					break;
				case 7:
					afiliado.setOcupacion(campo.getValue());
					LOG.info(counterLinea + " - " + "Ocupación: " + afiliado.getOcupacion());
					break;
				case 8:
					if (campo.getValue().toUpperCase().equals("MASCULINO")
							|| campo.getValue().toUpperCase().equals("FEMENINO")) {
						afiliado.setSexo(campo.getValue());
						LOG.info(counterLinea + " - " + "Sexo: " + afiliado.getSexo());
					} else {
						LOG.info(counterLinea + " - " + "El Sexo debe ser 'Masculino' ó 'Femenino'");
						log = counterLinea + " - " + "El Sexo debe ser 'Masculino' ó 'Femenino'";
						isValidAfiliado = false;
					}
					break;
				case 9:
					if (campo.getValue().length() == 0) {
						afiliado.setPais(null);
						LOG.info(counterLinea + " - " + "País: " + afiliado.getPais());
					} else if (campo.getValue().length() != 3) {
						LOG.info(counterLinea + " - " + "El país debe ser de 3 posiciones");
						log = counterLinea + " - " + "El país debe ser de 3 posiciones";
						isValidAfiliado = false;
					} else {
						afiliado.setPais(campo.getValue());
						isInteger = isInteger(afiliado.getPais());
						if (!isInteger) {
							LOG.info(counterLinea + " - " + "País: " + afiliado.getPais());
						} else {
							LOG.info(counterLinea + " - " + "El País no puede contener valores númericos");
							log = counterLinea + " - " + "El País no puede contener valores númericos";
							isValidAfiliado = false;
						}
					}
					break;
				case 10:
					if (campo.getValue().length() == 0) {
						afiliado.setCurp(null);
						LOG.info(counterLinea + " - " + "Curp: " + afiliado.getCurp());
					} else if (campo.getValue().length() != 18) {
						LOG.info(counterLinea + " - " + "El curp no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El curp no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						afiliado.setCurp(campo.getValue());
						LOG.info(counterLinea + " - " + "Curp: " + afiliado.getCurp());
					}
					break;
				case 11:
					if (campo.getValue().length() == 0) {
						afiliado.setNss(null);
						LOG.info(counterLinea + " - " + "Nss: " + afiliado.getNss());
					} else if (campo.getValue().length() != 11) {
						LOG.info(counterLinea + " - " + "El nss no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El nss no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						afiliado.setNss(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "NSS: " + afiliado.getNss());
					}
					break;
				case 12:
					if (campo.getValue().length() == 0) {
						LocalDate fechaNac = afiliado.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();

						rfc = new Rfc.Builder().name(afiliado.getNombre()).firstLastName(afiliado.getApellidoPaterno())
								.secondLastName(afiliado.getApellidoMaterno())
								.birthday(fechaNac.getDayOfMonth(), fechaNac.getMonthValue(), fechaNac.getYear())
								.build();
						Afiliado RfcExist = afiliadoService.getAfiliadoByRfc(rfc.toString());
						if(RfcExist!=null) {
							LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
							log = counterLinea + " - " + "El afiliado ya se encuentra registrado";
							isValidAfiliado = false;
						}
						afiliado.setRfc(rfc.toString());

						if (afiliado.getApellidoPaterno().equals(null) || afiliado.getApellidoPaterno().equals("")) {
							afiliado.setApellidoPaterno("-");
						}
						if (afiliado.getApellidoMaterno().equals(null) || afiliado.getApellidoMaterno().equals("")) {
							afiliado.setApellidoMaterno("-");
						}

						if (!isVigor) {

							Afiliado bAfiliado = afiliadoService.getAfiliadoByRfc(afiliado.getRfc());
							if (bAfiliado != null) {
								LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
								log = counterLinea + " - " + "El afiliado ya se encuentra registrado";

								isValidAfiliado = false;
							}
						} else {
							Afiliado bAfiliado = afiliadoService.getAfiliadoByRfc(afiliado.getRfc());
							if (bAfiliado != null) {
								LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
								log = counterLinea + " - " + "El afiliado ya se encuentra registrado";

								afiliadoService.updateEstatusAfiliadoById(bAfiliado.getId());

								isValidAfiliado = false;
							}
						}

						LOG.info(counterLinea + " - " + "RFC: " + afiliado.getRfc());
					} else if (campo.getValue().length() != 13) {
						LOG.info(counterLinea + " - " + "El RFC no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El RFC no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						Afiliado bAfiliado = afiliadoService.getAfiliadoByRfc(campo.getValue());
						if (bAfiliado != null) {
							LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
							log = counterLinea + " - " + "El afiliado ya se encuentra registrado";
							isValidAfiliado = false;
						} else {
							afiliado.setRfc(campo.getValue());
							LOG.info(counterLinea + " - " + "RFC: " + afiliado.getRfc());
						}
					}
					break;
				case 13:
					if (campo.getValue().length() == 0) {
						afiliado.setTelefonoFijo(null);
						LOG.info(counterLinea + " - " + "Telefono fijo: " + afiliado.getTelefonoFijo());
					} else {
						afiliado.setTelefonoFijo(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "Telefono fijo: " + afiliado.getTelefonoFijo());
					}
					break;
				case 14:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El telefono movil no puede quedar vacío");
						log = counterLinea + " - " + "El telefono movil no puede quedar vacío";
						isValidAfiliado = false;
						} else {
							afiliado.setTelefonoMovil(Long.parseLong(campo.getValue()));
							LOG.info(counterLinea + " - " + "Telefono móvil: " + afiliado.getTelefonoMovil());
					}
					break;
				case 15:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El email no puede quedar vacío");
						log = counterLinea + " - " + "El email no puede quedar vacío";
						isValidAfiliado = false;
					} else {
						isValid = isValidEmail(campo.getValue());

						if (isValid) {
							afiliado.setEmail(campo.getValue());
							LOG.info(counterLinea + " - " + "Email: " + afiliado.getEmail());
						} else {
							LOG.info(counterLinea + " - " + "Email inválido");
							log = counterLinea + " - " + "Email inválido";
							isValidAfiliado = false;
						}
					}
					break;
				case 16:
					afiliado.setDireccion(campo.getValue());
					LOG.info(counterLinea + " - " + "Dirección: " + afiliado.getDireccion());
					break;
				case 17:
					afiliado.setMunicipio(campo.getValue());
					LOG.info(counterLinea + " - " + "Municipio: " + afiliado.getMunicipio());
					break;
				case 18:
					if (campo.getValue().length() == 0) {
						afiliado.setCodigoPostal(null);
					} else if (campo.getValue().length() != 5) {
						LOG.info(counterLinea + " - " + "El código postal no cuenta con los dígitos correctos");
						log = counterLinea + " - " + "El código postal no cuenta con los dígitos correctos";
						isValidAfiliado = false;
					} else {
						Pattern pat = Pattern.compile("[0-9]*");
					     Matcher mat = pat.matcher(campo.getValue());
					     if(mat.matches()) {
					    	 afiliado.setCodigoPostal(campo.getValue());
								LOG.info(counterLinea + " - " + "Código Postal: " + afiliado.getCodigoPostal()); 
					     }else {
					    	 LOG.info(counterLinea + " - " + "El código postal solo deben ser numeros");
					    	 log = counterLinea + " - " + "El código postal solo deben ser numeros";
					    	 isValidAfiliado = false;
					     }
					}
					break;
				case 19:
					if (campo.getValue().length() == 0) {
						afiliado.setEntidadFederativa(null);
						LOG.info(counterLinea + " - " + "Entidad Federativa: " + afiliado.getEntidadFederativa());
					} else {
						isValid = isValidEntidadFederativa(campo.getValue());

						if (isValid) {
							afiliado.setEntidadFederativa(campo.getValue());
							LOG.info(counterLinea + " - " + "Entidad Federativa: " + afiliado.getEntidadFederativa());
						} else {
							LOG.info(counterLinea + " - " + "La entidad federativa " + campo.getValue() + " no existe");
							log = counterLinea + " - " + "La entidad federativa " + campo.getValue() + " no existe";
							isValidAfiliado = false;
						}
					}
					break;
				case 20:
					if (campo.getValue().length() == 0) {
						afiliado.setNumeroInfonavit(null);
					} else if (campo.getValue().length() != 11) {
						LOG.info(counterLinea + " - " + "El número de infonavit no cumple con los dígitos totales");
						log = counterLinea + " - " + "El número de infonavit no cumple con los dígitos totales";
						isValidAfiliado = false;
					} else {
						afiliado.setNumeroInfonavit(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "Número Infonavit: " + afiliado.getNumeroInfonavit());
					}
					break;
				case 21:
					if (campo.getValue().length() == 0) {
						afiliado.setFechaAfiliacion(null);
						LOG.info(counterLinea + " - " + "FechaAfiliacion: " + afiliado.getFechaAfiliacion());
					} else {
						isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

						if (isValid) {
							afiliado.setFechaAfiliacion(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
							LOG.info(counterLinea + " - " + "Fecha de Afiliación: " + afiliado.getFechaAfiliacion());
						} else {
							log = counterLinea + " - " + "Formato de fecha incorrecto para la fecha de afiliación, "
									+ "formato correcto: dd/mm/yyyy";
							LOG.info(counterLinea + " - " + "Formato de fecha incorrecto para la fecha de afiliación, "
									+ "formato correcto: dd/mm/yyyy");
							isValidAfiliado = false;
						}
					}
					break;
				case 22:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El servicio no puede quedar vacío");
						log = counterLinea + " - " + "El servicio no puede quedar vacío";
						isValidAfiliado = false;
					} else {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del servicio que se encuentra dentro de "
									+ "la hoja con el nombre 'Servicios' en el template que descargó";
						} else {
							Servicio servicio = servicioService.findById(Long.parseLong(campo.getValue()));

							if (servicio != null) {
								afiliado.setServicio(servicio);
								LOG.info(counterLinea + " - " + "Servicio: " + afiliado.getServicio().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El servicio " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El servicio " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 23:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "Proporcione un periodo");
						log = counterLinea + " - " + "Proporcione un periodo";
						isValidAfiliado = false;
					} else {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del periodo que se encuentra dentro de "
									+ "la hoja con el nombre 'Periodos' en el template que descargó";
						} else {
							Periodicidad periodo = periodicidadService.findById(Long.parseLong(campo.getValue()));

							if (periodo != null) {
								afiliado.setPeriodicidad(periodo);
								LOG.info(counterLinea + " - " + "Periodo: " + afiliado.getPeriodicidad().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El periodo " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El periodo " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 24:
					afiliado.setComentarios(campo.getValue());
					LOG.info(counterLinea + " - " + "Comentarios: " + afiliado.getComentarios());
					break;
				case 25:
					if (campo.getValue().length() == 0) {
						isBeneficiario = "No";
					} else {
						isBeneficiario = campo.getValue();
					}
					break;
				case 26:
					rfcAfiliado = campo.getValue();
					break;
				case 27:
					if (campo.getValue().length() > 0) {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del promotor que se encuentra dentro de "
									+ "la hoja con el nombre 'Promotor' en el template que descargó";
						} else {
							Promotor promotor = promotorService.findById(Long.parseLong(campo.getValue()));

							if (promotor != null) {
								afiliado.setPromotor(promotor);
								LOG.info(counterLinea + " - " + "Promotor: " + afiliado.getPromotor().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El promotor " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El promotor " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 28:
					if (campo.getValue().length() > 0) {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del cuenta que se encuentra dentro de "
									+ "la hoja con el nombre 'Cuenta' en el template que descargó";
						} else {
							Cuenta cuenta = cuentaService.findById(Long.parseLong(campo.getValue()));

							if (cuenta != null) {
								afiliado.setCuenta(cuenta);
								LOG.info(counterLinea + " - " + "Cuenta: " + afiliado.getCuenta().getRazonSocial());
							} else {
								LOG.info(counterLinea + " - " + "La cuenta " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "La cuenta " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 29:
					if (campo.getValue().length() > 0) {
						corte = Integer.parseInt(campo.getValue());
					}
					break;
				}

				if (isValidAfiliado) {
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

		if (!isVigor && !isConciliacion) {
			log = insertAfiliados(isValidAfiliado, afiliado, counterLinea);
		} else {
			log = insertAfiliadosVigor(isValidAfiliado, afiliado, counterLinea, idCuentaComercial);
		}

		return log;
	}

	/**
	 * Método que inserta los afiliados que no son vigor
	 *
	 * @param isValidAfiliado
	 * @param afiliado
	 * @param counterLinea
	 * @return
	 */

	private String insertAfiliados(boolean isValidAfiliado, Afiliado afiliado, Integer counterLinea) {

		try {

			if (isValidAfiliado) {
				if (afiliado.getRfc() == null) {
					LOG.info(counterLinea + " - " + "No se pudo armar el RFC");
					log = counterLinea + " - " + "No se pudo armar el RFC";
					isValidAfiliado = false;

				} else {

					if (afiliado.getServicio() == null || afiliado.getPeriodicidad() == null) {
						isValidAfiliado = false;
					} else {
						collator.setStrength(Collator.PRIMARY);
						if (isBeneficiario.toUpperCase().equals("SÍ")) {
							afiliado.setIsBeneficiario(true);
							afiliado.setEstatus(1);
							afiliado.setIsIncripcion(false);
							afiliado.setClave(generarClave.getClave(clave));
							afiliado.setFechaAlta(new Date());
							titular = afiliadoService.getAfiliadoByRfc(rfcAfiliado);

							if (titular != null) {

								Double saldoAcumuladoTitular = titular.getSaldoAcumulado();
								Double saldoAcumuladoBeneficiario = afiliado.getServicio().getCostoBeneficiario();

								saldoAcumuladoTitular = saldoAcumuladoTitular + saldoAcumuladoBeneficiario;

								titular.setSaldoAcumulado(saldoAcumuladoTitular);
								titular.setSaldoCorte(saldoAcumuladoTitular);
								afiliado.setInscripcion(new Double(0.0));
								afiliado.setServicio(titular.getServicio());
								afiliado.setCuenta(titular.getCuenta());
								afiliado.setFechaCorte(titular.getFechaCorte());
								afiliado.setPeriodicidad(titular.getPeriodicidad());

								afiliadoService.save(afiliado);
								afiliadoService.save(titular);
								afiliadoService.insertBeneficiarioUsingJpa(afiliado, titular.getId());
							} else {
								log = counterLinea + " - "
										+ "El beneficiario no se ha insertado ya que el Titular no se ha encontrado"
										+ " en el sistema";
								LOG.info(counterLinea + " - "
										+ "El beneficiario no se ha insertado ya que el Titular no se ha encontrado"
										+ " en el sistema");

								isValidAfiliado = false;
							}

						} else if (isBeneficiario.toUpperCase().equals("NO")) {
							afiliado.setIsBeneficiario(false);
							afiliado.setEstatus(1);
							afiliado.setIsIncripcion(false);
							afiliado.setClave(generarClave.getClave(clave));
							afiliado.setFechaAlta(new Date());
							if (afiliado.getFechaAfiliacion() == null) {
								LOG.info(counterLinea + " - " + "La fecha de afiliación no debe quedar vacío ");
								log = counterLinea + " - " + "La fecha de afiliación no debe quedar vacío";
								isValidAfiliado = false;
							} else {
								formatoMesYear = new SimpleDateFormat("MM/yyyy");
								fechaAfiliacion = formatoMesYear.format(afiliado.getFechaAfiliacion());
								fechaHoy = formatoMesYear.format(new Date());
								mYA = formatoMesYear.parse(fechaAfiliacion);
								mYH = formatoMesYear.parse(fechaHoy);
								formatoFecha = new SimpleDateFormat("dd");
								dia = formatoFecha.format(afiliado.getFechaAfiliacion());
								diaCorte = Integer.parseInt(dia);

								// evalua si esta dentro del mes o año actual
								if (mYH.equals(mYA)) {
									fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), diaCorte);
									afiliado.setFechaCorte(fechaCorte);
									afiliado.setSaldoCorte(0.0);

								}
								// evalua si esta fuera del mes o año actual
								if (mYH.after(mYA)) {
									fechaCorte = calcularFechas.calcularFechaAtrasada(afiliado.getFechaAfiliacion(),
											afiliado.getPeriodicidad(), diaCorte);
									afiliado.setFechaCorte(fechaCorte);
									afiliado.setSaldoCorte(0.0);
								}

								afiliado.setSaldoAcumulado(afiliado.getServicio().getCostoTitular());
								afiliado.setInscripcion(new Double(0.0));

								afiliadoService.save(afiliado);
							}

						}
					}
					if (!isValidAfiliado) {
						LOG.info(counterLinea + " - "
								+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
					} else {
						LOG.info(counterLinea + " - " + "El afiliado se ha insertado correctamente");
						log = counterLinea + " - " + "El afiliado se ha insertado correctamente";
					}
				}
			} else {
				LOG.info(counterLinea + " - "
						+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
			}
		} catch (Exception e) {
			LOG.info(counterLinea + " - " + "Error al momento de guardar el Afiliado", e);
			log = counterLinea + " - " + "Error al momento de guardar el Afiliado: " + e.getMessage();

		}

		return log;
	}

	/**
	 * Inserta Afiliados del archivo Vigor
	 *
	 * @param isValidAfiliado
	 * @param afiliado
	 * @param counterLinea
	 * @return
	 */

	private String insertAfiliadosVigor(boolean isValidAfiliado, Afiliado afiliado, Integer counterLinea,
			Long idCuentaComercial) {
		try {

			if (isValidAfiliado) {
				if (afiliado.getRfc() == null) {
					LOG.info(counterLinea + " - " + "No se pudo armar el RFC");
					log = counterLinea + " - " + "No se pudo armar el RFC";
					isValidAfiliado = false;

				} else {
					if (afiliado.getServicio() == null || afiliado.getPeriodicidad() == null) {
						isValidAfiliado = false;
					} else {
						collator.setStrength(Collator.PRIMARY);
						if (isBeneficiario.toUpperCase().equals("SÍ")) {
							afiliado.setIsBeneficiario(true);
							afiliado.setEstatus(1);
							afiliado.setIsIncripcion(false);
							afiliado.setClave(generarClave.getClave(clave));
							afiliado.setFechaAlta(new Date());
							titular = afiliadoService.getAfiliadoByRfc(rfcAfiliado);

							if (titular != null) {

								titular.setSaldoAcumulado(new Double(0.0));
								afiliado.setInscripcion(new Double(0.0));

								afiliado.setServicio(titular.getServicio());
								afiliado.setCuenta(titular.getCuenta());
								afiliado.setFechaCorte(titular.getFechaCorte());
								afiliado.setPeriodicidad(titular.getPeriodicidad());

								afiliadoService.save(afiliado);
								afiliadoService.save(titular);
								afiliadoService.insertBeneficiarioUsingJpa(afiliado, titular.getId());
							} else {
								log = counterLinea + " - "
										+ "El beneficiario no se ha insertado ya que el Titular no se ha encontrado"
										+ " en el sistema";
								LOG.info(counterLinea + " - "
										+ "El beneficiario no se ha insertado ya que el Titular no se ha encontrado"
										+ " en el sistema");

								isValidAfiliado = false;
							}
						} else if (isBeneficiario.toUpperCase().equals("NO")) {
							afiliado.setIsBeneficiario(false);
							afiliado.setEstatus(1);
							afiliado.setIsIncripcion(false);
							afiliado.setClave(generarClave.getClave(clave));
							afiliado.setFechaAlta(new Date());

							Cuenta cuenta = cuentaService.findById(idCuentaComercial);
							afiliado.setCuenta(cuenta);

							Date fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), corte);
							afiliado.setFechaCorte(fechaCorte);

							afiliado.setSaldoAcumulado(new Double(0.00));
							afiliado.setSaldoCorte(new Double(0.00));
							afiliado.setInscripcion(new Double(0.0));

							afiliadoService.save(afiliado);
						}
					}

					if (!isValidAfiliado) {
						LOG.info(counterLinea + " - "
								+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
					} else {
						LOG.info(counterLinea + " - " + "El afiliado se ha insertado correctamente");
						log = counterLinea + " - " + "El afiliado se ha insertado correctamente";
					}
				}
			} else {
				LOG.info(counterLinea + " - "
						+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
			}
		} catch (Exception e) {
			LOG.info(counterLinea + " - " + "Error al momento de guardar el Afiliado", e);
			log = counterLinea + " - " + "Error al momento de guardar el Afiliado: " + e.getLocalizedMessage();
		}

		return log;
	}
	
	public String evaluarMoneygramDatosList(boolean isVigor, boolean isConciliacion,boolean isMoneygram, Integer counterLinea,
			Map<Integer, String> campos) {

		Afiliado afiliado = new Afiliado();
		RelAfiliadoMoneygram relAfiMoneygram = new RelAfiliadoMoneygram();

		isValidAfiliado = true;
		rfc = null;


		for (Map.Entry<Integer, String> campo : campos.entrySet()) {
			try {

				switch (campo.getKey()) {
				case 0:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El nombre no puede quedar vacío");
						log = counterLinea + " - " + "El nombre no puede quedar vacío";
						isValidAfiliado = false;
					} else {
						afiliado.setNombre(campo.getValue());
						isInteger = isInteger(afiliado.getNombre());
						if (!isInteger) {
							isValid = containsVocal(afiliado.getNombre());
							if (isValid) {
								LOG.info(counterLinea + " - " + "Nombre: " + afiliado.getNombre());
							} else {
								log = counterLinea + " - " + "El nombre no contiene una vocal";
								LOG.info(counterLinea + " - " + "El nombre no contiene una vocal");
								isValidAfiliado = false;
							}

						} else {
							log = counterLinea + " - " + "El nombre no puede contener valores númericos";
							LOG.info(counterLinea + " - " + "El nombre no puede contener valores númericos");
							isValidAfiliado = false;
						}

					}
					break;
				case 1:
					afiliado.setApellidoPaterno(campo.getValue());
					isInteger = isInteger(afiliado.getApellidoPaterno());

					if (!isInteger) {
						isValid = containsVocal(afiliado.getApellidoPaterno());
						if (isValid) {
							LOG.info(counterLinea + " - " + "Apellido Paterno: " + afiliado.getApellidoPaterno());
						} else {
							log = counterLinea + " - " + "El Apellido Paterno no contiene una vocal";
							LOG.info(counterLinea + " - " + "El Apellido Paterno no contiene una vocal");
							isValidAfiliado = false;
						}
					} else {
						log = counterLinea + " - " + "El Apellido Paterno no puede contener valores númericos";
						LOG.info(counterLinea + " - " + "El Apellido Paterno no puede contener valores númericos");
						isValidAfiliado = false;
					}
					break;
				case 2:
					afiliado.setApellidoMaterno(campo.getValue());
					isInteger = isInteger(afiliado.getApellidoMaterno());
					if (!isInteger) {
						isValid = containsVocal(afiliado.getApellidoMaterno());
						if (isValid) {
							LOG.info(counterLinea + " - " + "Apellido Materno: " + afiliado.getApellidoMaterno());
						} else {
							log = counterLinea + " - " + "El Apellido Materno no contiene una vocal";
							LOG.info(counterLinea + " - " + "El Apellido Materno no contiene una vocal");
							isValidAfiliado = false;
						}
					} else {
						LOG.info(counterLinea + " - " + "El Apellido Materno no puede contener valores númericos");
						log = counterLinea + " - " + "El Apellido Materno no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 3:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "La fecha de nacimiento no debe quedar vacío");
						log = counterLinea + " - " + "La fecha de nacimiento no debe quedar vacío";
						isValidAfiliado = false;
					} else {
						isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

						if (isValid) {
							afiliado.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
							LOG.info(counterLinea + " - " + "Fecha de Nacimiento: " + afiliado.getFechaNacimiento());
						} else {
							log = counterLinea + " - " + "Formato de fecha incorrecto para la fecha de nacimiento, "
									+ "formato correcto dd/mm/yyyy";
							LOG.info(counterLinea + " - " + "Formato de fecha incorrecto para la fecha de nacimiento, "
									+ "formato correcto dd/mm/yyyy");
							isValidAfiliado = false;
						}
					}
					break;
				case 4:
					afiliado.setLugarNacimiento(campo.getValue());
					isInteger = isInteger(afiliado.getLugarNacimiento());
					if (!isInteger) {
						LOG.info(counterLinea + " - " + "Lugar de Nacimiento: " + afiliado.getLugarNacimiento());
					} else {
						LOG.info(counterLinea + " - " + "El Lugar de Nacimiento no puede contener valores númericos");
						log = counterLinea + " - " + "El Lugar de Nacimiento no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 5:
					afiliado.setEstadoCivil(campo.getValue());
					isInteger = isInteger(afiliado.getEstadoCivil());
					if (!isInteger) {
						LOG.info(counterLinea + " - " + "Estado Civil: " + afiliado.getEstadoCivil());
					} else {
						LOG.info(counterLinea + " - " + "El Estado Civil no puede contener valores númericos");
						log = counterLinea + " - " + "El Estado Civil no puede contener valores númericos";
						isValidAfiliado = false;
					}
					break;
				case 6:
					afiliado.setOcupacion(campo.getValue());
					LOG.info(counterLinea + " - " + "Ocupación: " + afiliado.getOcupacion());
					break;
				case 7:
					if (campo.getValue().toUpperCase().equals("MASCULINO")
							|| campo.getValue().toUpperCase().equals("FEMENINO")) {
						afiliado.setSexo(campo.getValue());
						LOG.info(counterLinea + " - " + "Sexo: " + afiliado.getSexo());
					} else {
						LOG.info(counterLinea + " - " + "El Sexo debe ser 'Masculino' ó 'Femenino'");
						log = counterLinea + " - " + "El Sexo debe ser 'Masculino' ó 'Femenino'";
						isValidAfiliado = false;
					}
					break;
				case 8:
					if (campo.getValue().length() == 0) {
						afiliado.setPais(null);
						LOG.info(counterLinea + " - " + "País: " + afiliado.getPais());
					} else if (campo.getValue().length() != 3) {
						LOG.info(counterLinea + " - " + "El país debe ser de 3 posiciones");
						log = counterLinea + " - " + "El país debe ser de 3 posiciones";
						isValidAfiliado = false;
					} else {
						afiliado.setPais(campo.getValue());
						isInteger = isInteger(afiliado.getPais());
						if (!isInteger) {
							LOG.info(counterLinea + " - " + "País: " + afiliado.getPais());
						} else {
							LOG.info(counterLinea + " - " + "El País no puede contener valores númericos");
							log = counterLinea + " - " + "El País no puede contener valores númericos";
							isValidAfiliado = false;
						}
					}
					break;
				case 9:
					if (campo.getValue().length() == 0) {
						afiliado.setCurp(null);
						LOG.info(counterLinea + " - " + "Curp: " + afiliado.getCurp());
					} else if (campo.getValue().length() != 18) {
						LOG.info(counterLinea + " - " + "El curp no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El curp no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						afiliado.setCurp(campo.getValue());
						LOG.info(counterLinea + " - " + "Curp: " + afiliado.getCurp());
					}
					break;
				case 10:
					if (campo.getValue().length() == 0) {
						afiliado.setNss(null);
						LOG.info(counterLinea + " - " + "Nss: " + afiliado.getNss());
					} else if (campo.getValue().length() != 11) {
						LOG.info(counterLinea + " - " + "El nss no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El nss no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						afiliado.setNss(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "NSS: " + afiliado.getNss());
					}
					break;
				case 11:
					if (campo.getValue().length() == 0) {
						LocalDate fechaNac = afiliado.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();

						rfc = new Rfc.Builder().name(afiliado.getNombre()).firstLastName(afiliado.getApellidoPaterno())
								.secondLastName(afiliado.getApellidoMaterno())
								.birthday(fechaNac.getDayOfMonth(), fechaNac.getMonthValue(), fechaNac.getYear())
								.build();
						Afiliado RfcExist = afiliadoService.getAfiliadoByRfc(rfc.toString());
						if(RfcExist!=null) {
							LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
							log = counterLinea + " - " + "El afiliado ya se encuentra registrado";
							isValidAfiliado = false;
						}
						afiliado.setRfc(rfc.toString());
						
						if (afiliado.getApellidoPaterno().equals(null) || afiliado.getApellidoPaterno().equals("")) {
							afiliado.setApellidoPaterno("-");
						}
						if (afiliado.getApellidoMaterno().equals(null) || afiliado.getApellidoMaterno().equals("")) {
							afiliado.setApellidoMaterno("-");
						}
						LOG.info(counterLinea + " - " + "RFC: " + afiliado.getRfc());
					} else if (campo.getValue().length() != 13) {
						LOG.info(counterLinea + " - " + "El RFC no cuenta con la longitud correcta");
						log = counterLinea + " - " + "El RFC no cuenta con la longitud correcta";
						isValidAfiliado = false;
					} else {
						Afiliado bAfiliado = afiliadoService.getAfiliadoByRfc(campo.getValue());
						if (bAfiliado != null) {
							LOG.info(counterLinea + " - " + "El afiliado ya se encuentra registrado");
							log = counterLinea + " - " + "El afiliado ya se encuentra registrado";
							isValidAfiliado = false;
						} else {
							afiliado.setRfc(campo.getValue());
							LOG.info(counterLinea + " - " + "RFC: " + afiliado.getRfc());
						}
					}
					break;
				case 12:
					if (campo.getValue().length() == 0) {
						afiliado.setTelefonoFijo(null);
						LOG.info(counterLinea + " - " + "Telefono fijo: " + afiliado.getTelefonoFijo());
					} else {
						afiliado.setTelefonoFijo(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "Telefono fijo: " + afiliado.getTelefonoFijo());
					}
					break;
				case 13:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El telefono movil no puede quedar vacío");
						log = counterLinea + " - " + "El telefono movil no puede quedar vacío";
						isValidAfiliado = false;
						} else {
							afiliado.setTelefonoMovil(Long.parseLong(campo.getValue()));
							LOG.info(counterLinea + " - " + "Telefono móvil: " + afiliado.getTelefonoMovil());
					}
					break;
				case 14:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El email no puede quedar vacío");
						log = counterLinea + " - " + "El email no puede quedar vacío";
						isValidAfiliado = false;
					} else {
						isValid = isValidEmail(campo.getValue());

						if (isValid) {
							afiliado.setEmail(campo.getValue());
							LOG.info(counterLinea + " - " + "Email: " + afiliado.getEmail());
						} else {
							LOG.info(counterLinea + " - " + "Email inválido");
							log = counterLinea + " - " + "Email inválido";
							isValidAfiliado = false;
						}
					}
					break;
				case 15:
					afiliado.setDireccion(campo.getValue());
					LOG.info(counterLinea + " - " + "Dirección: " + afiliado.getDireccion());
					break;
				case 16:
					afiliado.setMunicipio(campo.getValue());
					LOG.info(counterLinea + " - " + "Municipio: " + afiliado.getMunicipio());
					break;
				case 17:
					if (campo.getValue().length() == 0) {
						afiliado.setCodigoPostal(null);
					} else if (campo.getValue().length() != 5) {
						LOG.info(counterLinea + " - " + "El código postal no cuenta con los dígitos correctos");
						log = counterLinea + " - " + "El código postal no cuenta con los dígitos correctos";
						isValidAfiliado = false;
					} else {
						Pattern pat = Pattern.compile("[0-9]*");
					     Matcher mat = pat.matcher(campo.getValue());
					     if(mat.matches()) {
					    	 afiliado.setCodigoPostal(campo.getValue());
								LOG.info(counterLinea + " - " + "Código Postal: " + afiliado.getCodigoPostal()); 
					     }else {
					    	 LOG.info(counterLinea + " - " + "El código postal solo deben ser numeros");
					    	 log = counterLinea + " - " + "El código postal solo deben ser numeros";
					    	 isValidAfiliado = false;
					     }
					}
					break;
				case 18:
					if (campo.getValue().length() == 0) {
						afiliado.setEntidadFederativa(null);
						LOG.info(counterLinea + " - " + "Entidad Federativa: " + afiliado.getEntidadFederativa());
					} else {
						isValid = isValidEntidadFederativa(campo.getValue());

						if (isValid) {
							afiliado.setEntidadFederativa(campo.getValue());
							LOG.info(counterLinea + " - " + "Entidad Federativa: " + afiliado.getEntidadFederativa());
						} else {
							LOG.info(counterLinea + " - " + "La entidad federativa " + campo.getValue() + " no existe");
							log = counterLinea + " - " + "La entidad federativa " + campo.getValue() + " no existe";
							isValidAfiliado = false;
						}
					}
					break;
				case 19:
					if (campo.getValue().length() == 0) {
						afiliado.setNumeroInfonavit(null);
					} else if (campo.getValue().length() != 11) {
						LOG.info(counterLinea + " - " + "El número de infonavit no cumple con los dígitos totales");
						log = counterLinea + " - " + "El número de infonavit no cumple con los dígitos totales";
						isValidAfiliado = false;
					} else {
						afiliado.setNumeroInfonavit(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "Número Infonavit: " + afiliado.getNumeroInfonavit());
					}
					break;
				case 20:
					if (campo.getValue().length() == 0) {
							LOG.info(counterLinea + " - " + "La fecha de afiliación no puede quedar vacío");
							log = counterLinea + " - " + "La fecha de afiliación no puede quedar vacío";
							isValidAfiliado = false;
						} else {
							isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

						if (isValid) {
							afiliado.setFechaAfiliacion(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
							LOG.info(counterLinea + " - " + "Fecha de Afiliación: " + afiliado.getFechaAfiliacion());
						} else {
							log = counterLinea + " - " + "Formato de fecha incorrecto para la fecha de afiliación, "
									+ "formato correcto: dd/mm/yyyy";
							LOG.info(counterLinea + " - " + "Formato de fecha incorrecto para la fecha de afiliación, "
									+ "formato correcto: dd/mm/yyyy");
							isValidAfiliado = false;
						}
					}
					break;
				case 21:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "El servicio no puede quedar vacío");
						log = counterLinea + " - " + "El servicio no puede quedar vacío";
						isValidAfiliado = false;
					} else {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del servicio que se encuentra dentro de "
									+ "la hoja con el nombre 'Servicios' en el template que descargó";
						} else {
							Servicio servicio = servicioService.findById(Long.parseLong(campo.getValue()));

							if (servicio != null) {
								afiliado.setServicio(servicio);
								LOG.info(counterLinea + " - " + "Servicio: " + afiliado.getServicio().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El servicio " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El servicio " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 22:
					if (campo.getValue().length() == 0) {
						LOG.info(counterLinea + " - " + "Proporcione un periodo");
						log = counterLinea + " - " + "Proporcione un periodo";
						isValidAfiliado = false;
					} else {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del periodo que se encuentra dentro de "
									+ "la hoja con el nombre 'Periodos' en el template que descargó";
						} else {
							Periodicidad periodo = periodicidadService.findById(Long.parseLong(campo.getValue()));

							if (periodo != null) {
								afiliado.setPeriodicidad(periodo);
								LOG.info(counterLinea + " - " + "Periodo: " + afiliado.getPeriodicidad().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El periodo " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El periodo " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 23:
					afiliado.setComentarios(campo.getValue());
					LOG.info(counterLinea + " - " + "Comentarios: " + afiliado.getComentarios());
					break;
				case 24:
					if (campo.getValue().length() > 0) {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del promotor que se encuentra dentro de "
									+ "la hoja con el nombre 'Promotor' en el template que descargó";
						} else {
							Promotor promotor = promotorService.findById(Long.parseLong(campo.getValue()));

							if (promotor != null) {
								afiliado.setPromotor(promotor);
								LOG.info(counterLinea + " - " + "Promotor: " + afiliado.getPromotor().getNombre());
							} else {
								LOG.info(counterLinea + " - " + "El promotor " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "El promotor " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}else {
						log = counterLinea + " - " + "Proporcione el id del periodo que se encuentra dentro de "
								+ "la hoja con el nombre 'Periodos' en el template que descargó";
						isValidAfiliado = false;
					}
					break;
				case 25:
					if (campo.getValue().length() > 0) {

						if (!isString(campo.getValue())) {
							log = counterLinea + " - " + "Proporcione el id del cuenta que se encuentra dentro de "
									+ "la hoja con el nombre 'Cuenta' en el template que descargó";
						} else {
							Cuenta cuenta = cuentaService.findById(Long.parseLong(campo.getValue()));

							if (cuenta != null) {
								afiliado.setCuenta(cuenta);
								LOG.info(counterLinea + " - " + "Cuenta: " + afiliado.getCuenta().getRazonSocial());
							} else {
								LOG.info(counterLinea + " - " + "La cuenta " + campo.getValue()
										+ " no existe en el sistema");
								log = counterLinea + " - " + "La cuenta " + campo.getValue()
										+ " no existe en el sistema";
								isValidAfiliado = false;
							}
						}
					}
					break;
				case 26:
					if (campo.getValue().length() > 0) {
						corte = Integer.parseInt(campo.getValue());
					}
					break;
				
				case 27:
					LOG.info("Obteniendos los campos para la relAfiliadoMoneygram");
					if(campo.getValue().length() > 0) {
						relAfiMoneygram.setNombreContratante(campo.getValue());
						LOG.info(counterLinea + " - " + "Nombre contratante: " + relAfiMoneygram.getNombreContratante());
					}else {
						LOG.info(counterLinea + " - " + "El nombre contratante no puede quedar vacío");
						log = counterLinea + " - " + "El nombre contratante no puede quedar vacío";
						isValidAfiliado = false;
					}
					break;
				case 28:
					if(campo.getValue().length() > 0) {
						relAfiMoneygram.setEmailContratante(campo.getValue());
						LOG.info(counterLinea + " - " + "Correo electronico: " + relAfiMoneygram.getEmailContratante());
					}else {
						LOG.info(counterLinea + " - " + "El correo electrónico no puede quedar vacío");
						log = counterLinea + " - " + "El correo electrónico no puede quedar vacío";
						isValidAfiliado = false;
					}
					break;
				case 29:
					if(campo.getValue().length() > 0) {
						relAfiMoneygram.setTelefonoContratante(Long.parseLong(campo.getValue()));
						LOG.info(counterLinea + " - " + "Telefono móvil: " + relAfiMoneygram.getTelefonoContratante());
					}else {
						LOG.info(counterLinea + " - " + "El teléfono móvil no puede quedar vacío");
						log = counterLinea + " - " + "El teléfono móvil no puede quedar vacío";
						isValidAfiliado = false;
					}
					break;
				}
				if (isValidAfiliado) {
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

			log = insertAfiliadosMoneygram(isValidAfiliado, afiliado,relAfiMoneygram, counterLinea);
		

		return log;
	}
	
	private String insertAfiliadosMoneygram(boolean isValidAfiliado, Afiliado afiliado,RelAfiliadoMoneygram relAfiMoneygram, Integer counterLinea) {
		try {

			if (isValidAfiliado) {
				if (afiliado.getRfc() == null) {
					LOG.info(counterLinea + " - " + "No se pudo armar el RFC");
					log = counterLinea + " - " + "No se pudo armar el RFC";
					isValidAfiliado = false;

				} else {
					if (afiliado.getServicio() == null || afiliado.getPeriodicidad() == null) {
						isValidAfiliado = false;
					} else {
						collator.setStrength(Collator.PRIMARY);
							afiliado.setIsBeneficiario(false);
							afiliado.setEstatus(1);
							afiliado.setIsIncripcion(false);
							afiliado.setClave(generarClave.getClave(clave));
							afiliado.setFechaAlta(new Date());

							
							Date fechaCorte = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), corte);
							afiliado.setFechaCorte(fechaCorte);

							afiliado.setSaldoAcumulado(new Double(0.00));
							afiliado.setSaldoCorte(new Double(0.00));
							afiliado.setInscripcion(new Double(0.0));
							//genera idMoneygram
							String idMoneygram=generaIdMoneygram(afiliado);
							if(idMoneygram==null) {
								LOG.info(counterLinea + " - " +"No se pudo generar el idMoneygram para el afiliado: "+afiliado.getNombre() +" "+afiliado.getApellidoPaterno()+" "+afiliado.getApellidoMaterno());
								log = counterLinea + "-" +"No se pudo generar el idMoneygram para el afiliado: "+afiliado.getNombre() +" "+afiliado.getApellidoPaterno()+" "+afiliado.getApellidoMaterno();
								isValidAfiliado=false;
							}else {

								if(relAfiMoneygram.getNombreContratante()!=null && relAfiMoneygram.getEmailContratante()!=null
										&& relAfiMoneygram.getTelefonoContratante()>0) {
									afiliadoService.save(afiliado);
									relAfiMoneygram.setAfiliado(afiliado);
									relAfiMoneygram.setIdMoneygram(idMoneygram);
									relAfiMoneygramService.save(relAfiMoneygram);
								}else {
									LOG.info(counterLinea + " - " +"los datos nombre contratante,correo elétronico y telefóno no deben venir vacíos");
									log = counterLinea + "-" +"Los datos nombre contratante,correo elétronico y telefóno no deben venir vacíos ";
									isValidAfiliado=false;
								}
							}
							
					}
						

					if (!isValidAfiliado) {
						LOG.info(counterLinea + " - "
								+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
					} else {
						LOG.info(counterLinea + " - " + "El afiliado se ha insertado correctamente");
						log = counterLinea + " - " + "El afiliado se ha insertado correctamente";
					}
				}
			} else {
				LOG.info(counterLinea + " - "
						+ "El afiliado no se ha registrado, verifique los datos que sean correctos");
			}
		} catch (Exception e) {
			LOG.info(counterLinea + " - " + "Error al momento de guardar el Afiliado", e);
			log = counterLinea + " - " + "Error al momento de guardar el Afiliado: " + e.getLocalizedMessage();
		}

		return log;
	}

	
	private String generaIdMoneygram(Afiliado afiliado) {
		String idMoneygram= null;
		Empresa empresa = empresaService.findById(afiliado.getPromotor().getEmpresa().getId());
        Parametro parametro = parametroService.findById(ID_MONEYGRAM);
        
        if(empresa == null || parametro == null){
            LOG.error("error: El id de la empresa no se ha encontrado");
            return idMoneygram;
        }
        String valor = parametro.getValor();
        String clave = empresa.getClave();
        String clavePromotor = afiliado.getPromotor().getClave();
        Long consecutivoEmpresa = empresa.getConsecutivo();
        
     // Verificar si la empresa trae un consecutivo
        	if(consecutivoEmpresa == null){
        		consecutivoEmpresa = 1L;
        	}else{
        		consecutivoEmpresa = consecutivoEmpresa + 1;
        	}

        empresa.setConsecutivo(consecutivoEmpresa);
        empresaService.save(empresa);
        	String consecutivo = String.format("%0" + PADDING_SIZE + "d", consecutivoEmpresa);
        	 idMoneygram = valor + clave + clavePromotor + consecutivo;
        	
		return idMoneygram;
		
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
			return false;
		}

		return isValid;
	}

	/**
	 * Evalúa si el email está correcto
	 *
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pattern = Pattern.compile(emailRegex);
		if (email == null) {
			return false;
		}

		return pattern.matcher(email).matches();

	}

	/**
	 * Evalúa si la entidad es correcta
	 *
	 * @param entidadFederativa
	 * @return
	 */
	private boolean isValidEntidadFederativa(String entidadFederativa) {

		boolean isValid = false;

		List<String> entidades = afiliadoService.getAllEstados();

		for (String entidad : entidades) {
			if (entidadFederativa.equals(entidad)) {
				isValid = true;
				break;
			} else {
				isValid = false;
			}
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

	/**
	 * Verificación de los campos que soliciten id's
	 *
	 * @param number
	 * @return
	 */
	public boolean isString(String number) {
		try {
			Long id = Long.parseLong(number);
		} catch (NumberFormatException ne) {
			return false;
		}
		return true;
	}

	/**
	 * Verificación de la cadena si contiene una vocal
	 *
	 * @param cadena
	 * @return
	 */
	public boolean containsVocal(String cadena) {

		int totalVocales = cadena.replaceAll("[^AEIOUaeiouÁÉÍÓÚáéíóú]", "").length();

		if (totalVocales >= 1) {
			return true;
		} else {
			return false;
		}

	}

}