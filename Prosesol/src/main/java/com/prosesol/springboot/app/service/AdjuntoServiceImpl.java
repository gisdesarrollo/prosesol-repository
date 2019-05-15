package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Adjunto;
import com.prosesol.springboot.app.entity.dao.IAdjuntoDao;

@Service
public class AdjuntoServiceImpl implements IAdjuntoService{

	@Autowired
	private IAdjuntoDao adjuntoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Adjunto> findAll() {
		return (List<Adjunto>)adjuntoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Adjunto findById(Long id) {
		return adjuntoDao.findById(id).orElse(null);
	}

	@Override
	public void save(Adjunto adjunto) {
		adjuntoDao.save(adjunto);		
	}

	@Override
	public void deleteById(Long id) {
		adjuntoDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Adjunto> getAdjuntoCorreo(Long id) {
		return adjuntoDao.getAdjuntoCorreo(id);
	}

}
