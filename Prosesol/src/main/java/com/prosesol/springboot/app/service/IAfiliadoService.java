package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.util.Paises;

public interface IAfiliadoService {

	public List<Afiliado> findAll();
	
	public void save(Afiliado afiliado);
	
	public void delete(Long id);
	
	public Afiliado findById(Long id);
	
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long idAfiliado);
	
	public void insertBeneficiarioUsingJpa(Afiliado beneficiario, Long id);
	
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id);
	
	public List<String> getAllEstados();
	
	public List<Paises> getAllPaises();
	
	
}
