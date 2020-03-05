package com.prosesol.springboot.app.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.validator.ValidarMesesImpl;

@Service
public class CalcularFecha {
	
	protected static final Log logger = LogFactory.getLog(CalcularFecha.class);

	private int periodoTiempo;
	private int diaCorte;
	
	private Boolean isLeapYear;
	
	private LocalDate tiempoActual;
	private LocalDate tiempoModificado;
	private LocalDate fechaCorte;
	
	private GregorianCalendar calendar;
	
	@Autowired
	private ValidarMesesImpl validarMeses;

	public Date calcularFechas(Periodicidad periodo, Integer corte) {
		
		init();

		switch (periodo.getPeriodo().toUpperCase()) {
		case "MENSUAL":

			logger.info("Entra al perido MENSUAL");

			periodoTiempo = 1;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
						
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}
			
			break;

		case "BIMESTRAL":

			logger.info("Entra al perido BIMESTRAL");

			periodoTiempo = 2;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "TRIMESTRAL":

			logger.info("Entra al perido TRIMESTRAL");

			periodoTiempo = 3;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth().plus(1), diaCorte);

			break;
		case "CUATRIMESTRAL":

			logger.info("Entra al perido CUATRIMESTRAL");

			periodoTiempo = 4;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "SEMESTRAL":

			logger.info("Entra al perido SEMESTRAL");

			periodoTiempo = 6;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "ANUAL":

			logger.info("Entra al perido ANUAL");

			periodoTiempo = 12;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;

		default:
			new Exception();

		}

		return java.util.Date.from(fechaCorte.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
	}
	
public Date calcularFechaAtrasada(Date fecha,Periodicidad periodo, Integer corte) {
		
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		String fechaA=formatoFecha.format(fecha);
		initFA(fechaA);

		switch (periodo.getPeriodo().toUpperCase()) {
		case "MENSUAL":

			logger.info("Entra al perido MENSUAL");

			periodoTiempo = 1;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
						
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}
			
			break;

		case "BIMESTRAL":

			logger.info("Entra al perido BIMESTRAL");

			periodoTiempo = 2;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "TRIMESTRAL":

			logger.info("Entra al perido TRIMESTRAL");

			periodoTiempo = 3;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth().plus(1), diaCorte);

			break;
		case "CUATRIMESTRAL":

			logger.info("Entra al perido CUATRIMESTRAL");

			periodoTiempo = 4;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "SEMESTRAL":

			logger.info("Entra al perido SEMESTRAL");

			periodoTiempo = 6;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;
		case "ANUAL":

			logger.info("Entra al perido ANUAL");

			periodoTiempo = 12;
			diaCorte = corte;

			tiempoModificado = tiempoActual.plusMonths(periodoTiempo);
			isLeapYear = calendar.isLeapYear(tiempoModificado.getYear());
			
			if(validarMeses.has30Days(tiempoModificado.getMonth()) && diaCorte == 31) {
				logger.info("Mes con 30 días");
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 30);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && isLeapYear && diaCorte == 31 || diaCorte == 30){
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 29);
			}else if(validarMeses.isFebruary(tiempoModificado.getMonth()) && diaCorte == 31 || diaCorte == 30 || diaCorte == 29) {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), 28);
			}else {
				fechaCorte = LocalDate.of(tiempoModificado.getYear(), tiempoModificado.getMonth(), diaCorte);
			}

			break;

		default:
			new Exception();

		}

		return java.util.Date.from(fechaCorte.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
	}
	
	public void init() {
		
		tiempoActual = LocalDate.now();
		tiempoModificado = null;
		fechaCorte = null;
		calendar = new GregorianCalendar();
		
	}
	
	public void initFA(String fecha) {
		
		tiempoActual = LocalDate.parse(fecha);
		tiempoModificado = null;
		fechaCorte = null;
		calendar = new GregorianCalendar();
		
	}
	
}
