package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Servicio;

import java.util.List;


public interface IServicioService {

	public List<Servicio> findAll();
	
	public void save(Servicio servicio);
	
	public void delete(Long id);
	
	public Servicio findById(Long id);
	
	public String[] getVariablesServicio();

	public String getNombreByIdIncidencia(Long id);
}
