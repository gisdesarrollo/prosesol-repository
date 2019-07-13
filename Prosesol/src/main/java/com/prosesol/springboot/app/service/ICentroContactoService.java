package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.CentroContacto;

public interface ICentroContactoService {

	public List<CentroContacto> findAll();
	
	public CentroContacto findById(Long id);
	
	public void save(CentroContacto centroContacto);
	
	public void deleteById(Long id);
	
}
