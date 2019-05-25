package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.entity.dao.IPerfilDao;

@Service
public class PerfilServiceImpl implements IPerfilService{

	@Autowired
	private IPerfilDao perfilDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Perfil> findAll() {
		return (List<Perfil>)perfilDao.findAll();
	}

	@Override
	@Transactional
	public void save(Perfil perfil) {
		perfilDao.save(perfil);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		perfilDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Perfil findById(Long id) {
		return perfilDao.findById(id).orElse(null);
	}

}
