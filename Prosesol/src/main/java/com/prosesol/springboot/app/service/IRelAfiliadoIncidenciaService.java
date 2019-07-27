package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;

public interface IRelAfiliadoIncidenciaService {

	public List<RelAfiliadoIncidencia> findAll();
	
	public RelAfiliadoIncidencia findById(Long id);
	
	public void save(RelAfiliadoIncidencia relAfiliadoIncidencia);
	
	public void deleteById(Long id);
	
	public List<RelAfiliadoIncidencia> getRelAfiliadoIncidenciaByIdIncidencia(Long id);
	
}
