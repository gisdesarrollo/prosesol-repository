package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IAfiliadoService {

	public List<Afiliado> findAll();
	
	public void save(Afiliado afiliado);
	
	public void delete(Long id);
	
	public Afiliado findById(Long id);
	
}
