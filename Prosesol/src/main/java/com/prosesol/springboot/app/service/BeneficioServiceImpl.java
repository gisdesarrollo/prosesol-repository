package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.dao.IBeneficioDao;

@Service
public class BeneficioServiceImpl implements IBeneficioService{

	@Autowired
	private IBeneficioDao beneficioDao;

	@Override
	@Transactional(readOnly = true)
	public Beneficio findById(Long id) {
		return beneficioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Beneficio beneficio) {
		beneficioDao.save(beneficio);		
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		beneficioDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Beneficio> findAll() {
		return (List<Beneficio>)beneficioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Beneficio> getBeneficiosByIdServicio(Long id) {
		return beneficioDao.getBeneficiosByIdServicio(id);
	}
	
	

}
