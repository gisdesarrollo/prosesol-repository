package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Beneficio;

public interface IBeneficioService {
	
	public Beneficio findById(Long id);
	
	public void save(Beneficio beneficio);
		
	public void deleteById(Long id);
	
	public List<Beneficio> findAll();
	
	public List<Beneficio> getBeneficiosByIdServicio(Long id);

}
