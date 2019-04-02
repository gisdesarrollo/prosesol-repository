package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Beneficiario;

public interface IBeneficiarioService {

	public List<Beneficiario> findAll();
	
	public void delete(Long id);
	
	public Beneficiario findById(Long id);
	
	public void save(Beneficiario beneficiario);
}
