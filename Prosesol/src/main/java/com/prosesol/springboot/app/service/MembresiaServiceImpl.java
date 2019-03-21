package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Membresia;
import com.prosesol.springboot.app.entity.dao.IMembresiaDao;

@Service
public class MembresiaServiceImpl implements IMembresiaService{

	@Autowired
	private IMembresiaDao membresiaDao;

	@Override
	public List<Membresia> findAll() {
		return (List<Membresia>)membresiaDao.findAll();
	}

	@Override
	public void save(Membresia membresia) {
		membresiaDao.save(membresia);
	}

	@Override
	public void delete(Membresia membresia) {
		membresiaDao.delete(membresia);
	}

	@Override
	@Transactional(readOnly = true)
	public Membresia findById(Long id) {
		return membresiaDao.findById(id).orElse(null);
	}
	
}
