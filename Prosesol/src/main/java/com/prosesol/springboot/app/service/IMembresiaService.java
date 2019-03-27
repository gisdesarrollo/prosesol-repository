package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Membresia;


public interface IMembresiaService {

	public List<Membresia> findAll();
	
	public void save(Membresia membresia);
	
	public void delete(Long id);
	
	public Membresia findById(Long id);
	
}
