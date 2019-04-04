package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficiario;
import com.prosesol.springboot.app.entity.dao.IAfiliadoDao;
import com.prosesol.springboot.app.entity.dao.IBeneficiarioDao;

@Service
public class AfiliadoServiceImpl implements IAfiliadoService{

	@Autowired
	private IAfiliadoDao iAfiliadoDao;
	
	@Autowired
	private IBeneficiarioDao iBeneficiarioDao;
	

	@Override
	@Transactional(readOnly=true)
	public List<Afiliado> findAll() {
		return (List<Afiliado>)iAfiliadoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Afiliado afiliado) {
		iAfiliadoDao.save(afiliado);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		iAfiliadoDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Afiliado findById(Long id) {
		return iAfiliadoDao.findById(id).orElse(null);
	}

	@Override
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long idAfiliado) {
		
		for(Afiliado beneficiario : iAfiliadoDao.getBeneficiarioByIdByIsBeneficiario(idAfiliado)) {
			System.out.println(beneficiario.toString());
		}
		
		return iAfiliadoDao.getBeneficiarioByIdByIsBeneficiario(idAfiliado);
	}

	

	

}
