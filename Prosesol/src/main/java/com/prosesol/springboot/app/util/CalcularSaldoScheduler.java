package com.prosesol.springboot.app.util;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//@Configuration
//@EnableScheduling
public class CalcularSaldoScheduler {

	@Scheduled(fixedDelay = 5000)
	public void scheduledFixedDelayTask() {
		System.out.println("Method executed at every 5 seconds. Current time is :: " + new Date());
	}
}
