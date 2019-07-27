package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Candidato;
import com.prosesol.springboot.app.util.Paises;

public interface ICandidatoService {

	public List<Candidato> findAll();
	
	public Candidato findById(Long id);
	
	public void save(Candidato candidato);
	
	public void deleteById(Long id);
	
	public List<String> getAllEstados();
	
	public List<Paises> getAllPaises();
	
}
