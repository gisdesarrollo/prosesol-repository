package com.prosesol.springboot.app.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.EmailServiceImpl;
import com.prosesol.springboot.app.service.IAfiliadoService;

@Configuration
@EnableScheduling
public class CalcularSaldoScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CalcularSaldoScheduler.class);
	
	private Date fechaActual = new Date();
		
	@Autowired
	private IAfiliadoService afiliadoService;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Scheduled(fixedDelay = 100000)
	@Transactional
	public void calcularSaldoScheduler() throws ParseException, MessagingException, IOException {
		
		Mail mail = new Mail();
		
		System.out.println("Method executed at every 10 seconds. Current time is :: " + new Date());
		
		String pattern = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		
		List<Afiliado> afiliados = afiliadoService.findAll();
		List<Afiliado> beneficiarios = new ArrayList<Afiliado>();
		
		for(Afiliado afiliado : afiliados) {
			if(afiliado.getIsBeneficiario().equals(false)) {
				logger.info("Tipo de afiliado: " + afiliado.getIsBeneficiario().toString());
				
				String fecha = dateFormat.format(fechaActual);
				
				Date fechaModificada = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
				
				if(afiliado.getFechaCorte().compareTo(fechaModificada) == 0) {
				    
					logger.info("Afiliados con falta de pago: " + afiliado.toString());
					
					List<Double> saldos = new ArrayList<Double>();
					
					beneficiarios = afiliadoService.getAfiliadoAssignedBeneficiario(afiliado.getId());
					
					for(Afiliado beneficiario : beneficiarios) {
						saldos.add(beneficiario.getSaldoAcumulado());	
					}
					
					Double suma = new Double(0);
					
					for(int i = 0; i < saldos.size(); i++) {
						Double getSaldo = saldos.get(i);
						suma = Double.sum(suma, getSaldo);
						System.out.println("Saldo por cada beneficiario: " + getSaldo);
						System.out.println("Suma de los saldos acumulados" + suma);
					}
					
					Double saldoAcumulado = Double.sum(afiliado.getSaldoAcumulado(), suma);
					
					afiliado.setSaldoAcumulado(saldoAcumulado);
					afiliado.setSaldoCorte(saldoAcumulado);
					
					afiliadoService.save(afiliado);
					
					String body = "Estimado: " + afiliado.getNombre() + " (Prueba de suspensión de servicio)";
					
//					emailServiceImpl.sendText("luis.morales@gisconsultoria.com", afiliado.getEmail(), 
//							"SUSPENSIÓN DE SERVICIO", body);
//					emailServiceImpl.sendSimpleEmail("prosesol@example.com", "luis.morales@gisconsultoria.com", 
//							"SUSPENSIÓN DE SERVICIO", body);
					
					mail.setTo("luis.morales@gisconsultoria.com");
					mail.setFrom("prosesol@exampl.com");
					mail.setSubject(body);
					
					emailServiceImpl.sendSimpleMessage(mail);
				}
			}
		}
		
	}
}
