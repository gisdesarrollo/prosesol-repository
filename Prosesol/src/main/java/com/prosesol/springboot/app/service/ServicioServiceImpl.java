package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.dao.IServicioDao;

@Service
public class ServicioServiceImpl implements IServicioService{

	@Autowired
	private IServicioDao membresiaDao;

	@Override
	public List<Servicio> findAll() {
		return (List<Servicio>)membresiaDao.findAll();
	}

	@Override
	public void save(Servicio membresia) {
		membresiaDao.save(membresia);
	}

	@Override
	public void delete(Long id) {
		membresiaDao.deleteById(id);;
	}

	@Override
	@Transactional(readOnly = true)
	public Servicio findById(Long id) {
		return membresiaDao.findById(id).orElse(null);
	}
	
}
