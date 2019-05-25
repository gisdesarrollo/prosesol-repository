package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Perfil;

public interface IPerfilService {

	public List<Perfil> findAll();
	
	public void save(Perfil perfil);
	
	public void deleteById(Long id);
	
	public Perfil findById(Long id);
	
}
