package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Correo;
import com.prosesol.springboot.app.entity.dao.ICorreoDao;

@Service
public class CorreoServiceImpl implements ICorreoService{

	@Autowired
	private ICorreoDao correoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Correo> findAll() {
		return (List<Correo>)correoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Correo findById(Long id) {
		return correoDao.findById(id).orElse(null);
	}

	@Override
	public void save(Correo correo) {
		correoDao.save(correo);		
	}

	@Override
	public void delete(Long id) {
		correoDao.deleteById(id);	
	}

	@Override
	@Transactional(readOnly = true)
	public Correo getTemplateCorreoByName(String name) {
		return correoDao.getTemplateCorreByName(name);
	}

}
