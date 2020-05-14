package com.prosesol.springboot.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.prosesol.springboot.app.repository.InsertaCandidatoIntoAfiliadoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Candidato;
import com.prosesol.springboot.app.entity.dao.ICandidatoDao;
import com.prosesol.springboot.app.util.Estados;
import com.prosesol.springboot.app.util.Paises;

@Service
public class CandidatoServiceImpl implements ICandidatoService{

	@Autowired
	private ICandidatoDao candidatoDao;

	@Autowired
	private InsertaCandidatoIntoAfiliadoRepository candidatoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Candidato> findAll() {
		return (List<Candidato>)candidatoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Candidato findById(Long id) {
		return candidatoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Candidato candidato) {
		candidatoDao.save(candidato);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		candidatoDao.deleteById(id);
	}
	
	@Override
	public List<String> getAllEstados() {
		
		Estados estados = new Estados();
		
		return estados.getEstados();
	}

	@Override
	public List<Paises> getAllPaises() {
		return new ArrayList<>(Arrays.asList(Paises.values()));
	}

	@Override
	@Transactional(readOnly=true)
	public List<Candidato> getCandidatoBySearchNombreCompleto(String nombre, String apellidoPaterno,
			String apellidoMaterno) {
		return candidatoDao.getCandidatoBySearchNombreCompleto(nombre, apellidoPaterno, apellidoMaterno) ;
	}

	@Override
	public boolean insertaCandidatoIntoAfiliado(Long idCandidato,String clave ) {
		return candidatoRepository.insertAfiliado(idCandidato,clave);
		
	}	

}
