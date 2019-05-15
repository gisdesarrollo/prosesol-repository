package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Correo;

public interface ICorreoService {

	public List<Correo> findAll();
	
	public Correo findById(Long id);
	
	public void save(Correo correo);
	
	public void deleteById(Long id);
	
	public Correo getTemplateCorreoByName(String name);
	
}
