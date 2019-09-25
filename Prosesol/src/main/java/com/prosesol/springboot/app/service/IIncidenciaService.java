package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.custom.IncidenciaCustom;

import java.util.List;

public interface IIncidenciaService {

	public List<Incidencia> findAll();
	
	public Incidencia findById(Long id);
	
	public void save(Incidencia incidencia);
	
	public void deleteById(Long id);

	public List<Incidencia> getIncidenciasByUserId(Long id);

	public List<IncidenciaCustom> getHistorialIncidenciaByIdAfiliado(Long id);
}
