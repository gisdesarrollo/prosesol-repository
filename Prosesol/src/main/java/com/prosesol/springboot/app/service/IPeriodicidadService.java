package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.util.Eventos;

public interface IPeriodicidadService {

	public List<Periodicidad> findAll();
	
	public void save(Periodicidad periodicidad);
	
	public void deleteById(Long id);
	
	public Periodicidad findById(Long id);
	
	public List<Eventos> getAllEventos();
	
	public String[] getVariablesPeriodo();
	
}
