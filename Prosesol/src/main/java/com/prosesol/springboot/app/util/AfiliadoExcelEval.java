package com.prosesol.springboot.app.util;

import com.josketres.rfcfacil.Rfc;
import com.prosesol.springboot.app.entity.*;
import com.prosesol.springboot.app.service.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AfiliadoExcelEval {

    protected final Log LOG = LogFactory.getLog(AfiliadoExcelEval.class);

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

    private int corte;
    private Long idAfiliado;

    private String isBeneficiario;
    private String rfcAfiliado;

    private Date fechaNacimiento;

    private LocalDate localDate;

    private DateTimeFormatter dateTimeFormatter;

    private Rfc rfc;

    private Collator collator = Collator.getInstance(new Locale("es"));

    private boolean isValidAfiliado;
    private boolean isValid;

    public List<String> evaluarDatosList(Map<Integer, String> campos){

        List<String> log = new ArrayList<String>();
        Afiliado afiliado = new Afiliado();

        isValidAfiliado = true;

        rfc = null;

        if(campos.size() < 26){
            isBeneficiario = "No";
        }

        for(Map.Entry<Integer, String> campo : campos.entrySet()){
            try {
                if (campo.getKey().equals(0)) {
                    if (campo.getValue().equals(null) || campo.getValue().equals("")) {
                        LOG.info("El nombre no puede quedar vació");
                        log.add("El campo no puede quedar vacío");
                        isValidAfiliado = false;

                        break;
                    } else {
                        afiliado.setNombre(campo.getValue());
                        boolean isInteger = isInteger(afiliado.getNombre());
                        if(!isInteger){
                            LOG.info("Nombre: " + afiliado.getNombre());
                        }else{
                            log.add("El nombre no puede contener valores númericos");
                            LOG.info("El nombre no puede contener valores númericos");
                            isValidAfiliado = false;
                            break;
                        }

                    }
                }
                if (campo.getKey() == 1) {
                    if (campo.getValue().equals(null) || campo.getValue().equals("")) {
                        LOG.info("El Apellido Paterno no puede quedar vacío");
                        log.add("El Apellido Paterno no puede quedar vacío");
                        isValidAfiliado = false;

                        break;
                    } else {
                        afiliado.setApellidoPaterno(campo.getValue());
                        boolean isInteger = isInteger(afiliado.getApellidoPaterno());

                        if(!isInteger){
                            LOG.info("Apellido Paterno: " + afiliado.getApellidoPaterno());
                        }else{
                            log.add("El Apellido Paterno no puede contener valores númericos");
                            LOG.info("El Apellido Paterno no puede contener valores númericos");
                            isValidAfiliado = false;
                            break;
                        }
                    }
                }
                if (campo.getKey() == 2) {
                    if (campo.getValue().equals(null) || campo.getValue().equals("")) {
                        LOG.info("El Apellido Materno no puede quedar vacío");
                        log.add("El Apellido Materno no puede quedar vacío");
                        isValidAfiliado = false;

                        break;
                    } else {
                        afiliado.setApellidoMaterno(campo.getValue());
                        boolean isInteger = isInteger(afiliado.getApellidoMaterno());
                        if(!isInteger){
                            LOG.info("Apellido Materno: " + afiliado.getApellidoMaterno());
                        }else{
                            LOG.info("El Apellido Materno no puede contener valores númericos");
                            log.add("El Apellido Materno no puede contener valores númericos");
                            isValidAfiliado = false;
                            break;
                        }

                    }
                }
                if (campo.getKey() == 3) {

                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        LOG.info("La fecha de nacimiento no debe quedar vacío");
                        log.add("La fecha de nacimiento no debe quedar vacío");
                        isValidAfiliado = false;

                        break;
                    }else{
                        boolean isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

                        if(isValid){
                            afiliado.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
                            LOG.info("Fecha de Nacimiento: " + afiliado.getFechaNacimiento());
                        }else{
                            log.add("Formato de fecha incorrecto, formato correcto dd/mm/yyyy");
                            LOG.info("Formato de fecha incorrecto, formato correcto dd/mm/yyyy");
                            isValidAfiliado = false;

                            break;
                        }
                    }
                }
                if (campo.getKey() == 4) {
                    afiliado.setLugarNacimiento(campo.getValue());
                    boolean isInteger = isInteger(afiliado.getLugarNacimiento());
                    if(!isInteger){
                        LOG.info("Lugar de Nacimiento: " + afiliado.getLugarNacimiento());
                    }else{
                        LOG.info("El Lugar de Nacimiento no puede contener valores númericos");
                        log.add("El Lugar de Nacimiento no puede contener valores númericos");
                        isValidAfiliado = false;
                        break;
                    }

                }
                if (campo.getKey() == 5) {
                    afiliado.setEstadoCivil(campo.getValue());
                    boolean isInteger = isInteger(afiliado.getEstadoCivil());
                    if(!isInteger){
                        LOG.info("Estado Civil: " + afiliado.getEstadoCivil());
                    }else{
                        LOG.info("El Estado Civil no puede contener valores númericos");
                        log.add("El Estado Civil no puede contener valores númericos");
                        isValidAfiliado = false;
                        break;
                    }

                }
                if (campo.getKey() == 6) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setNumeroDependientes(null);
                        LOG.info("Número de Dependientes: " + afiliado.getNumeroDependientes());
                    }else{
                        afiliado.setNumeroDependientes(Integer.parseInt(campo.getValue()));
                        LOG.info("Número de Dependientes: " + afiliado.getNumeroDependientes());
                    }
                }
                if (campo.getKey() == 7) {
                    afiliado.setOcupacion(campo.getValue());
                    LOG.info("Ocupación: " + afiliado.getOcupacion());
                }
                if (campo.getKey() == 8) {
                    if(campo.getValue().toUpperCase().equals("MASCULINO") ||
                            campo.getValue().toUpperCase().equals("FEMENINO")){
                        afiliado.setSexo(campo.getValue());
                        LOG.info("Sexo: " + afiliado.getSexo());
                    }else{
                        LOG.info("El Sexo debe ser 'Masculino' ó 'Femenino'");
                        log.add("El Sexo debe ser 'Masculino' ó 'Femenino'");
                        isValidAfiliado = false;

                        break;
                    }
                }
                if (campo.getKey() == 9) {

                    System.out.println("Longitud del país" + campo.getValue().length());

                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setPais(null);
                        LOG.info("País: " + afiliado.getPais());
                    }else if(campo.getValue().length() != 3){
                        LOG.info("El país debe ser de 3 posiciones");
                        log.add("El país debe ser de 3 posiciones");
                        isValidAfiliado = false;
                        break;
                    }else{
                        afiliado.setPais(campo.getValue());
                        boolean isInteger = isInteger(afiliado.getPais());
                        if(!isInteger){
                            LOG.info("País: " + afiliado.getPais());
                        }else{
                            LOG.info("El País no puede contener valores númericos");
                            log.add("El País no puede contener valores númericos");
                            isValidAfiliado = false;
                            break;
                        }
                    }

                }
                if (campo.getKey() == 10) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setCurp(null);
                        LOG.info("Curp: " + afiliado.getCurp());
                    }else if(campo.getValue().length() != 18){
                       LOG.info("El curp no cuenta con la longitud correcta");
                       log.add("El curp no cuenta con la longitud correcta");
                        isValidAfiliado = false;

                        break;
                    }else{
                        afiliado.setCurp(campo.getValue());
                        LOG.info("Curp: " + afiliado.getCurp());
                    }
                }
                if (campo.getKey() == 11) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setNss(null);
                        LOG.info("Nss: " + afiliado.getNss());
                    }else if(campo.getValue().length() != 11){
                        LOG.info("El nss no cuenta con la longitud correcta");
                        log.add("El nss no cuenta con la longitud correcta");
                        isValidAfiliado = false;

                        break;
                    }else{
                        afiliado.setNss(Long.parseLong(campo.getValue()));
                        LOG.info("NSS: " + afiliado.getNss());
                    }
                }
                if (campo.getKey() == 12) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        LocalDate fechaNac = afiliado.getFechaNacimiento().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        rfc = new Rfc.Builder()
                                .name(afiliado.getNombre())
                                .firstLastName(afiliado.getApellidoPaterno())
                                .secondLastName(afiliado.getApellidoMaterno())
                                .birthday(fechaNac.getDayOfMonth(), fechaNac.getMonthValue(), fechaNac.getYear())
                                .build();

                        afiliado.setRfc(rfc.toString());
                        LOG.info("RFC: " + afiliado.getRfc());
                    }else if(campo.getValue().length() != 13){
                        LOG.info("El RFC no cuenta con la longitud correcta");
                        log.add("El RFC no cuenta con la longitud correcta");
                        isValidAfiliado = false;

                        break;
                    }else{
                        afiliado.setRfc(campo.getValue());
                        LOG.info("RFC: " + afiliado.getRfc());
                    }
                }
                if (campo.getKey() == 13) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setTelefonoFijo(null);
                        LOG.info("Telefono fijo: " + afiliado.getTelefonoFijo());
                    }else {
                        afiliado.setTelefonoFijo(Long.parseLong(campo.getValue()));
                        LOG.info("Telefono fijo: " + afiliado.getTelefonoFijo());
                    }
                }
                if (campo.getKey() == 14) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setTelefonoMovil(null);
                        LOG.info("Telefono móvil: " + afiliado.getTelefonoMovil());
                    }else {
                        afiliado.setTelefonoMovil(Long.parseLong(campo.getValue()));
                        LOG.info("Telefono móvil: " + afiliado.getTelefonoMovil());
                    }
                }
                if (campo.getKey() == 15) {

                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setEmail(null);
                        LOG.info("Email: " + afiliado.getEmail());
                    }else{
                        isValid = isValidEmail(campo.getValue());

                        if(isValid){
                            afiliado.setEmail(campo.getValue());
                            LOG.info("Email: " + afiliado.getEmail());
                        }else{
                            LOG.info("Email inválido");
                            log.add("Email inválido");
                            isValidAfiliado = false;

                            break;
                        }
                    }
                }
                if (campo.getKey() == 16) {
                    afiliado.setDireccion(campo.getValue());
                    LOG.info("Dirección: " + afiliado.getDireccion());
                }
                if (campo.getKey() == 17) {
                    afiliado.setMunicipio(campo.getValue());
                    LOG.info("Municipio: " + afiliado.getMunicipio());
                }
                if (campo.getKey() == 18) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setCodigoPostal(null);
                    }else if(campo.getValue().length() != 5){
                        LOG.info("El código postal no cuenta con los dígitos correctos");
                        log.add("El código postal no cuenta con los dígitos correctos");
                        isValidAfiliado = false;

                        break;
                    }else{
                        afiliado.setCodigoPostal(Long.parseLong(campo.getValue()));
                        LOG.info("Código Postal: " + afiliado.getCodigoPostal());
                    }
                }
                if (campo.getKey() == 19) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setEntidadFederativa(null);
                        LOG.info("Entidad Federativa: " + afiliado.getEntidadFederativa());
                    }else{
                        isValid = isValidEntidadFederativa(campo.getValue());

                        if(isValid){
                            afiliado.setEntidadFederativa(campo.getValue());
                            LOG.info("Entidad Federativa: " + afiliado.getEntidadFederativa());
                        }else{
                            LOG.info("La entidad federativa " + campo.getValue() + " no existe");
                            log.add("La entidad federativa " + campo.getValue() + " no existe");
                            isValidAfiliado = false;

                            break;
                        }
                    }
                }
                if (campo.getKey() == 20) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setNumeroInfonavit(null);
                    }else if(campo.getValue().length() != 11){
                        LOG.info("El número de infonavit no cumple con los dígitos totales");
                        log.add("El número de infonavit no cumple con los dígitos totales");
                        isValidAfiliado = false;

                        break;
                    }else{
                        afiliado.setNumeroInfonavit(Long.parseLong(campo.getValue()));
                        LOG.info("Número Infonavit: " + afiliado.getNumeroInfonavit());
                    }
                }
                if (campo.getKey() == 21) {

                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        afiliado.setFechaAfiliacion(null);
                    }else{
                        isValid = isValidFormat("dd/MM/yyyy", campo.getValue());

                        if(isValid){
                            afiliado.setFechaAfiliacion(new SimpleDateFormat("dd/MM/yyyy").parse(campo.getValue()));
                            LOG.info("Fecha de Afiliación: " + afiliado.getFechaAfiliacion());
                        }else{
                            log.add("Formato de fecha incorrecto, formato correcto: dd/mm/yyyy");
                            LOG.info("Formato de fecha incorrecto, formato correcto: dd/mm/yyyy");
                            isValidAfiliado = false;

                            break;
                        }
                    }
                }
                if (campo.getKey() == 22) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        LOG.info("El servicio no puede quedar vacío");
                        log.add("El servicio no puede quedar vacío");
                        isValidAfiliado = false;

                        break;
                    }else{
                        Servicio servicio = getServicioByNombre(campo.getValue());

                        if(servicio != null){
                            afiliado.setServicio(servicio);
                            LOG.info("Servicio: " + afiliado.getServicio().getNombre());
                        }else{
                            LOG.info("El servicio " + campo.getValue() + " no existe en el sistema");
                            log.add("El servicio " + campo.getValue() + " no existe en el sistema");
                            isValidAfiliado = false;

                            break;
                        }
                    }
                }
                if (campo.getKey() == 23) {

                    Periodicidad periodo = getPeriodoByNombre(campo.getValue());

                    if(periodo != null){
                        afiliado.setPeriodicidad(periodo);
                        LOG.info("Periodo: " + afiliado.getPeriodicidad().getNombre());
                    }else{
                        LOG.info("El periodo " + campo.getValue() + " no existe en el sistema");
                        log.add("El periodo " + campo.getValue() + " no existe en el sistema");
                        isValidAfiliado = false;

                        break;
                    }

                }
                if (campo.getKey() == 24) {
                    afiliado.setComentarios(campo.getValue());
                    LOG.info("Comentarios: " + afiliado.getComentarios());
                }
                if (campo.getKey() == 25) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)) {
                        isBeneficiario = "No";
                    }else{
                        isBeneficiario = campo.getValue();
                    }

                }
                if (campo.getKey() == 26) {
                    rfcAfiliado = campo.getValue();
                }
                if (campo.getKey() == 27) {
                    Promotor promotor = getPromotorByNombre(campo.getValue());

                    if(promotor != null){
                        afiliado.setPromotor(promotor);
                        LOG.info("Promotor: " + afiliado.getPromotor().getNombre());
                    }else{
                        LOG.info("El promotor " + campo.getValue() + " no existe en el sistema");
                        log.add("El promotor " + campo.getValue() + " no existe en el sistema");
                        isValidAfiliado = false;

                        break;
                    }
                }
                if (campo.getKey() == 28) {
                    Cuenta cuenta = getCuentaByNombre(campo.getValue());

                    if(cuenta != null){
                        afiliado.setCuenta(cuenta);
                        LOG.info("Cuenta: " + afiliado.getCuenta().getRazonSocial());
                    }else{
                        LOG.info("La cuenta " + campo.getValue() + " no existe en el sistema");
                        log.add("La cuenta " + campo.getValue() + " no existe en el sistema");
                        isValidAfiliado = false;

                        break;
                    }
                }
                if (campo.getKey() == 29) {
                    if(campo.getValue().equals("") || campo.getValue().equals(null)){
                        LOG.info("El corte no debe quedar vacío");
                        log.add("El corte no debe quedar vacío");
                        isValidAfiliado = false;
                        break;
                    }else{
                        corte = Integer.parseInt(campo.getValue());
                    }

                }


            }catch(IllegalArgumentException e){
                LOG.error("Error al momento de leer el archivo", e);
                log.add("Error al momento de leer el archivo" + e.getMessage());
            }catch (ParseException pe){
                LOG.error("Error al momento de convertir la fecha: ", pe);
                log.add("Error al momento de convertir la fecha: " + pe.getMessage());
            }catch(Exception e){
                LOG.error("Error al momento de leer el archivo", e);
                log.add("Error al momento de leer el archivo" + e.getMessage());
            }
        }

        try {
            Long idAfiliadoByRfc = afiliadoService.getIdAfiliadoByRfc(afiliado.getRfc());
            if(idAfiliadoByRfc != null && idAfiliadoByRfc > 0){
                LOG.info("El afiliado ya se encuentra registrado");
                log.add("El afiliado ya se encuentra registrado");
            }else if(!isValidAfiliado){
                log.add("El afiliado no se ha registrado, verifique los datos que sean correctos");
                LOG.info("El afiliado no se ha registrado, verifique los datos que sean correctos");
            }else{
                collator.setStrength(Collator.PRIMARY);
                if(isBeneficiario.equals("Sí")){
                    afiliado.setIsBeneficiario(true);
                    afiliado.setEstatus(1);
                    afiliado.setClave(getClaveAfiliado());
                    afiliado.setFechaAlta(new Date());
                    idAfiliado = afiliadoService.getIdAfiliadoByRfc(rfcAfiliado);

                    afiliadoService.insertBeneficiarioUsingJpa(afiliado, idAfiliado);
                }else if(isBeneficiario.equals("No")){
                    afiliado.setIsBeneficiario(false);
                    afiliado.setEstatus(1);
                    afiliado.setClave(getClaveAfiliado());
                    afiliado.setFechaAlta(new Date());

                    afiliadoService.save(afiliado);
                }
            }
        }catch (Exception e){
            LOG.info("Error al momento de guardar el Afiliado", e);
            log.add("Error al momento de guardar el Afiliado: " + e.getMessage());
        }

        LOG.info("Termina la inserción del Afiliado por carga masiva");

        return null;
    }


    /**
     * Evalúa si el formato de fecha es el correcto
     * @param format
     * @param value
     * @return
     */
    private static boolean isValidFormat(String format, String value){

        boolean isValid = false;
        Date date = null;

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(value);

            if(value.equals(dateFormat.format(date))){
                isValid = true;
            }else{
                isValid = false;
            }
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return isValid;
    }

    /**
     * Evalúa si el email está correcto
     * @param email
     * @return
     */
    private boolean isValidEmail(String email){

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        if(email == null){
            return false;
        }

        return pattern.matcher(email).matches();

    }

    /**
     * Evalúa si la entidad es correcta
     * @param entidadFederativa
     * @return
     */
    private boolean isValidEntidadFederativa(String entidadFederativa){

        boolean isValid = false;

        List<String> entidades = afiliadoService.getAllEstados();

        for(String entidad : entidades){
            if(entidadFederativa.equals(entidad)){
                isValid = true;
                break;
            }else{
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Evalúa si el servicio existe en la BBDD
     * @param servicio
     * @return
     */
    private Servicio getServicioByNombre(String servicio){
        List<Servicio> listServicios = servicioService.findAll();
        Servicio nServicio = new Servicio();

        for(Servicio s : listServicios){
            if(s.getNombre().equals(servicio)){
               nServicio = s;
               break;
            }
        }

        return nServicio;
    }

    /**
     * Evalúa si existe el periodo en la BBDD
     * @param periodo
     * @return
     */
    private Periodicidad getPeriodoByNombre(String periodo){
        List<Periodicidad> listPeriodos = periodicidadService.findAll();
        Periodicidad nPeriodo = new Periodicidad();

        for(Periodicidad p : listPeriodos){
            if(p.getNombre().equals(periodo)){
                nPeriodo = p;
                break;
            }
        }

        return nPeriodo;
    }

    /**
     * Evalúa si el promotor existe en la BBDD
     * @param promotor
     * @return
     */
    private Promotor getPromotorByNombre(String promotor){
        List<Promotor> listPromotor = promotorService.findAll();
        Promotor nPromotor = new Promotor();

        for(Promotor p : listPromotor){
            if(p.getNombre().equals(promotor)){
                nPromotor = p;
                break;
            }
        }

        return nPromotor;
    }

    /**
     * Evalúa si la cuenta existe en la BBDD
     * @param cuenta
     * @return
     */
    private Cuenta getCuentaByNombre(String cuenta){
        List<Cuenta> listCuenta = cuentaService.findAll();
        Cuenta nCuenta = new Cuenta();

        for(Cuenta c : listCuenta){
            if(c.getRazonSocial().equals(cuenta)){
                nCuenta = c;
                break;
            }
        }

        return nCuenta;
    }

    /**
     * Verifica los campos de tipo string que no sean númericos
     * @param cadena
     * @return
     */
    private boolean isInteger(String cadena){
        if(NumberUtils.isCreatable(cadena)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Genera la clave del Afiliado
     * @return
     */
    private String getClaveAfiliado() {

        String claveAfiliado = "PR-";

        for (int i = 0; i < 10; i++) {
            claveAfiliado += (clave.charAt((int) (Math.random() * clave.length())));
        }

        return claveAfiliado;
    }
}
