package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Adjunto;

public interface IAdjuntoService{

	public List<Adjunto> findAll();
	
	public Adjunto findById(Long id);
	
	public void save(Adjunto adjunto);
	
	public void deleteById(Long id);
	
	public List<Adjunto> getAdjuntoCorreo(Long id);
	
}
