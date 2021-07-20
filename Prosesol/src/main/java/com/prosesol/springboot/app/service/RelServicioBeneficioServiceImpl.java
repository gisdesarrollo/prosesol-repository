package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.dao.IRelServicioBeneficioDao;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

@Service
public class RelServicioBeneficioServiceImpl implements IRelServicioBeneficioService{

	@Autowired
	private IRelServicioBeneficioDao relServicioBeneficioDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<RelServicioBeneficio> findAll() {
		return (List<RelServicioBeneficio>)relServicioBeneficioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public RelServicioBeneficio findById(Long id) {
		return relServicioBeneficioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(RelServicioBeneficio relServicioBeneficio) {
		relServicioBeneficioDao.save(relServicioBeneficio);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		relServicioBeneficioDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long id) {
		return relServicioBeneficioDao.getRelServicioBeneficioByIdServicio(id);
	}

	@Override
	@Transactional
	public void deleteBeneficioByIdBeneficioAndIdServicio(Long idServicio, Long idBeneficio) {
		relServicioBeneficioDao.deleteBeneficioByIdBeneficioAndIdServicio(idServicio, idBeneficio);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RelServicioBeneficio> getBeneficioByIdAfiliado(Long idAfiliado) {
		return relServicioBeneficioDao.getBeneficioByIdAfiliado(idAfiliado);
	}

	@Override
	@Transactional
	public void removeBeneficiobyIdBeneficio(Long idBeneficio) {
		relServicioBeneficioDao.removeBeneficiobyIdBeneficio(idBeneficio);
	}

	@Override
	public RelServicioBeneficio getRelServicioBeneficioByIdServcioAndIdBeneficio(Long idServicio, Long idBeneficio) {
		return relServicioBeneficioDao.getRelServicioBeneficioByIdServcioAndIdBeneficio(idServicio, idBeneficio);
	}

	@Override
	@Transactional
	public void deleteAllRelServicioBeneficioByIdServicio(Long idServicio) {
		relServicioBeneficioDao.deleteAllRelServicioBeneficioByIdServicio(idServicio);
		
	}


}
