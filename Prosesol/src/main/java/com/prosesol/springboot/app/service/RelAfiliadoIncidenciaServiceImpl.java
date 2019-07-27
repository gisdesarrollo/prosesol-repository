package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.dao.IRelAfiliadoIncidenciaDao;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;

@Service
public class RelAfiliadoIncidenciaServiceImpl implements IRelAfiliadoIncidenciaService{

	@Autowired
	private IRelAfiliadoIncidenciaDao relAfiliadoIncidenciaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<RelAfiliadoIncidencia> findAll() {
		return (List<RelAfiliadoIncidencia>)relAfiliadoIncidenciaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public RelAfiliadoIncidencia findById(Long id) {
		return relAfiliadoIncidenciaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(RelAfiliadoIncidencia relAfiliadoIncidencia) {
		relAfiliadoIncidenciaDao.save(relAfiliadoIncidencia);		
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		relAfiliadoIncidenciaDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RelAfiliadoIncidencia> getRelAfiliadoIncidenciaByIdIncidencia(Long id) {
		return relAfiliadoIncidenciaDao.getRelAfiliadoIncidenciaByIdIncidencia(id);
	}

}
