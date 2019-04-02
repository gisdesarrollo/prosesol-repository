package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Beneficiario;
import com.prosesol.springboot.app.entity.dao.IBeneficiarioDao;

@Service
public class BeneficiarioServiceImpl implements IBeneficiarioService{

	@Autowired
	private IBeneficiarioDao beneficiarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Beneficiario> findAll() {
		return (List<Beneficiario>)beneficiarioDao.findAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		beneficiarioDao.deleteById(id);;
	}

	@Override
	@Transactional(readOnly = true)
	public Beneficiario findById(Long id) {
		return beneficiarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Beneficiario beneficiario) {
		beneficiarioDao.save(beneficiario);
	}
	
	

}
