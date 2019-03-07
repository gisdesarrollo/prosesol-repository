package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.dao.IAfiliadoDao;

@Service
public class AfiliadoServiceImpl implements IAfiliadoService{

	@Autowired
	private IAfiliadoDao iAfiliadoDao;

	@Override
	@Transactional(readOnly=true)
	public List<Afiliado> findAll() {
		// TODO Auto-generated method stub
		return (List<Afiliado>)iAfiliadoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Afiliado afiliado) {
		// TODO Auto-generated method stub
		iAfiliadoDao.save(afiliado);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		iAfiliadoDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Afiliado findById(Long id) {
		// TODO Auto-generated method stub
		return iAfiliadoDao.findById(id).orElse(null);
	}	

}
