package com.prosesol.springboot.app.service;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.dao.IServicioDao;

@Service
public class ServicioServiceImpl implements IServicioService {

	@Autowired
	private IServicioDao servicioDao;

	@Override
	@Transactional(readOnly = true)
	public List<Servicio> findAll() {
		return (List<Servicio>) servicioDao.findAll();
	}

	@Override
	@Transactional
	public void save(Servicio servicio) {
		servicioDao.save(servicio);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		servicioDao.deleteById(id);
		;
	}

	@Override
	@Transactional(readOnly = true)
	public Servicio findById(Long id) {
		return servicioDao.findById(id).orElse(null);
	}

	@Override
	public String[] getVariablesServicio() {

		Field campos[] = Servicio.class.getDeclaredFields();
		String variablesServicio[] = new String[campos.length];

		for (int i = 0; i < campos.length; i++) {
			variablesServicio[i] = campos[i].getName();
		}

		return variablesServicio;

	}

}
