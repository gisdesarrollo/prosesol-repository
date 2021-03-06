package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

public interface IRelServicioBeneficioService {

	public List<RelServicioBeneficio> findAll();
	
	public RelServicioBeneficio findById(Long id);
	
	public void save(RelServicioBeneficio relServicioBeneficio);
	
	public void deleteById(Long id);
	
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long id);
	
	public void deleteBeneficioByIdBeneficioAndIdServicio(Long idServicio, Long idBeneficio);
	
	public List<RelServicioBeneficio> getBeneficioByIdAfiliado(Long idAfiliado);
	
	public void removeBeneficiobyIdBeneficio(Long idBeneficio);
	
	public RelServicioBeneficio getRelServicioBeneficioByIdServcioAndIdBeneficio(Long idServicio,Long idBeneficio); 
	
	public void deleteAllRelServicioBeneficioByIdServicio(Long idServicio);
}
