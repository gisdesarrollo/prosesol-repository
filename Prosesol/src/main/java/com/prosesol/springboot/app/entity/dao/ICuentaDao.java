package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Cuenta;

public interface ICuentaDao extends CrudRepository<Cuenta, Long>{
	
}
