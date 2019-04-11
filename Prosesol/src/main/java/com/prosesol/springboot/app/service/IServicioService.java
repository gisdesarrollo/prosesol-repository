package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Servicio;


public interface IServicioService {

	public List<Servicio> findAll();
	
	public void save(Servicio membresia);
	
	public void delete(Long id);
	
	public Servicio findById(Long id);
	
}
