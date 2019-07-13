package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.CentroContacto;
import com.prosesol.springboot.app.entity.dao.ICentroContactoDao;

@Service
public class CentroContactoServiceImpl implements ICentroContactoService{

	@Autowired
	private ICentroContactoDao centroContactoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<CentroContacto> findAll() {
		return (List<CentroContacto>)centroContactoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public CentroContacto findById(Long id) {
		return centroContactoDao.findById(id).orElse(null);
	}

	@Override
	public void save(CentroContacto centroContacto) {
		centroContactoDao.save(centroContacto);
	}

	@Override
	public void deleteById(Long id) {
		centroContactoDao.deleteById(id);
	}

}
