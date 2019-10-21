package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;

import java.util.List;

public interface IRelAfiliadoIncidenciaService {

	public List<RelAfiliadoIncidencia> findAll();
	
	public RelAfiliadoIncidencia findById(Long id);
	
	public void save(RelAfiliadoIncidencia relAfiliadoIncidencia);
	
	public void deleteById(Long id);

}
