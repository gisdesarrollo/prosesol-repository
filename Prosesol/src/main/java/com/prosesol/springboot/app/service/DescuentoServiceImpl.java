package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prosesol.springboot.app.entity.Descuento;
import com.prosesol.springboot.app.entity.dao.IDescuentoDao;

@Service
public class DescuentoServiceImpl implements IDescuentoService{

	@Autowired
	private IDescuentoDao descuentoDao;
	
	@Override
	public List<Descuento> findAll() {
		return (List<Descuento>)descuentoDao.findAll();
	}

	@Override
	public void save(Descuento descuento) {
		descuentoDao.save(descuento);
	}

	@Override
	public void deleteById(Long id) {
		descuentoDao.deleteById(id);
	}

	@Override
	public Descuento findById(Long id) {
		return descuentoDao.findById(id).orElse(null);
	}

}
