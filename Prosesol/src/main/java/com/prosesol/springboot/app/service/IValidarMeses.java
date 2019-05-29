package com.prosesol.springboot.app.service;

import java.time.Month;

public interface IValidarMeses {

	public boolean has30Days(Month month);
	
	public boolean isFebruary(Month month);
	
}
