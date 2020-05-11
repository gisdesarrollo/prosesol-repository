package com.prosesol.springboot.app.util.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.prosesol.springboot.app.service.ILogCMService;

@Configuration
@EnableScheduling
public class DeleteLogSheduler {
	
	protected static final Log LOG = LogFactory.getLog(DeleteLogSheduler.class);
	  
	
	@Autowired
	private ILogCMService logService;
	
	@Scheduled(cron = "0 0 12 * * WED",zone = "America/Mexico_City")
	public void deleteLogs() {
		Calendar semana = Calendar.getInstance();
		semana.add(Calendar.DATE,-6);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String  fecha_semanal = dateFormat.format(semana.getTime());
		String fecha_hoy = dateFormat.format(new Date());
		 
		LOG.info("Se ejecuta cron para la eliminación de los logs");
		logService.deleteAllLogs(fecha_semanal,fecha_hoy);
		
	}
	

}
