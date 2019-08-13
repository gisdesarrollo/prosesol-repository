package com.prosesol.springboot.app.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Periodicidad;
import com.prosesol.springboot.app.entity.dao.IPeriodicidadDao;
import com.prosesol.springboot.app.util.Eventos;

@Service
public class PeriodicidadServiceImpl implements IPeriodicidadService{

	@Autowired
	private IPeriodicidadDao periodicidadDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Periodicidad> findAll() {
		return (List<Periodicidad>)periodicidadDao.findAll();
	}

	@Override
	@Transactional
	public void save(Periodicidad periodicidad) {
		periodicidadDao.save(periodicidad);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Periodicidad findById(Long id) {
		return periodicidadDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		periodicidadDao.deleteById(id);
		
	}
	
	@Override
	public List<Eventos> getAllEventos() {
		
		List<Eventos> eventos = new ArrayList<Eventos>(Arrays.asList(Eventos.values()));
		
		return eventos;
	}

	@Override
	public String[] getVariablesPeriodo() {
		
		Field campos[] = Periodicidad.class.getDeclaredFields();
		String variablesPeriodo[] = new String[campos.length];
		
		for(int i = 0; i < campos.length; i++) {
			variablesPeriodo[i] = campos[i].getName();
		}
		
		return variablesPeriodo;

	}

}
