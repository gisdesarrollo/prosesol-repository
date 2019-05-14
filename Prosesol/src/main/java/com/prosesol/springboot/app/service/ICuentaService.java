package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Cuenta;

public interface ICuentaService {

	public List<Cuenta> findAll();
	
	public void save(Cuenta cuenta);
	
	public void delete(Long id);
	
	public Cuenta findById(Long id);
	
	public String[] getVariablesCuenta();	
}
