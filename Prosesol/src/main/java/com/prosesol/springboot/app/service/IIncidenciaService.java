package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Incidencia;

public interface IIncidenciaService {

	public List<Incidencia> findAll();
	
	public Incidencia findById(Long id);
	
	public void save(Incidencia incidencia);
	
	public void deleteById(Long id);
	
	public List<Incidencia> getIncidenciasByUserName(String username);	
}
