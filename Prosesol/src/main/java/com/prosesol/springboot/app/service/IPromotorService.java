package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Promotor;

public interface IPromotorService {

	public List<Promotor> findAll();
	
	public void save(Promotor promotor);
	
	public void delete(Long id);
	
	public Promotor findById(Long id);
	
}
