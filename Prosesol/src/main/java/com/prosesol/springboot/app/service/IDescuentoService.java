package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Descuento;

public interface IDescuentoService{

	public List<Descuento> findAll();
	
	public void save(Descuento descuento);
	
	public void deleteById(Long id);
	
	public Descuento findById(Long id);
	
}
