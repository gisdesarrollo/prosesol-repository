package com.prosesol.springboot.app.service;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Promotor;
import com.prosesol.springboot.app.entity.dao.IPromotorDao;

@Service
public class PromotorServiceImpl implements IPromotorService {

	@Autowired
	private IPromotorDao promotorDao;

	@Override
	@Transactional(readOnly = true)
	public List<Promotor> findAll() {
		return (List<Promotor>) promotorDao.findAll();
	}

	@Override
	@Transactional
	public void save(Promotor promotor) {
		promotorDao.save(promotor);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		promotorDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Promotor findById(Long id) {
		return promotorDao.findById(id).orElse(null);
	}

	@Override
	public String[] getVariablesPromotor() {
		
		Field campos[] = Promotor.class.getDeclaredFields();
		String variablesPromotor[] = new String[campos.length];
		
		for(int i = 0; i < campos.length; i++) {
			variablesPromotor[i] = campos[i].getName();
		}

		return variablesPromotor;
	}

	@Override
	public Promotor findByClave(String clave) {
		return promotorDao.findByClave(clave);
	}

}
