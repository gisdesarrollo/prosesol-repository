package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

public interface IRelServicioBeneficioService {

	public List<RelServicioBeneficio> findAll();
	
	public RelServicioBeneficio findById(Long id);
	
	public void save(RelServicioBeneficio relServicioBeneficio);
	
	public void deleteById(Long id);
	
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long id);
	
	public void updateRelServicioBeneficio(Long idServicio, Long idBeneficio, Double costo, String descripcion, Long idServicioBeneficio);
	
}
