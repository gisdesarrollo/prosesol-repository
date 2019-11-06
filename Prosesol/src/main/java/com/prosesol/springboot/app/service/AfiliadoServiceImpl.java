package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.entity.dao.IAfiliadoDao;
import com.prosesol.springboot.app.exception.CustomExcelException;
import com.prosesol.springboot.app.repository.AfiliadoRepository;
import com.prosesol.springboot.app.repository.BeneficiarioRepository;
import com.prosesol.springboot.app.util.Estados;
import com.prosesol.springboot.app.util.Paises;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AfiliadoServiceImpl implements IAfiliadoService{

	@Autowired
	private IAfiliadoDao iAfiliadoDao;
	
	@Autowired
	private BeneficiarioRepository beneficiarioRepository;
	
	@Autowired
	private AfiliadoRepository afiliadoRepository;

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
		return iAfiliadoDao.getBeneficiarioByIdByIsBeneficiario(idAfiliado);
	}

	@Override
	public void insertBeneficiarioUsingJpa(Afiliado beneficiario, Long id) {
		beneficiarioRepository.insertBeneficiario(beneficiario, id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id) {
		return iAfiliadoDao.getAfiliadoAssignedBeneficiario(id);
	}
	
	@Override
	public List<String> getAllEstados() {
		
		Estados estados = new Estados();
		
		return estados.getEstados();
	}

	@Override
	public List<Paises> getAllPaises() {
		
		List<Paises> paises = new ArrayList<Paises>(Arrays.asList(Paises.values()));
		
		return paises;
	}

	@Override
	public String[] getVariablesAfiliado() {
				
		Field campos[] = Afiliado.class.getDeclaredFields();
		String variablesAfiliado[] = new String[campos.length];
		
		for(int i = 0; i < campos.length; i++) {
			variablesAfiliado[i] = campos[i].getName();
		}
		
		
		return variablesAfiliado;
	}

	@Override
	@Transactional(readOnly = true)
	public Afiliado getAfiliadoByRfc(String rfc) {
		return iAfiliadoDao.getAfiliadoByRfc(rfc);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AfiliadoCustom> getAfiliadoByParams(String[] campos, Long idCcUsuario) {
		return afiliadoRepository.getAfiliadoByParams(campos, idCcUsuario);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Afiliado> getAfiliadoBySearchNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno) {
		return iAfiliadoDao.getAfiliadoBySearchNombreCompleto(nombre, apellidoPaterno, apellidoMaterno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Afiliado> getAfiliadosByFechaCorte(String fecha) {
		return iAfiliadoDao.getAfiliadosByFechaCorte(fecha);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Afiliado> getAfiliadosPagoPendiente() {
		return iAfiliadoDao.getAfiliadosPagoPendiente();
	}

	@Override
	@Transactional(readOnly = true)
	public Long getIdAfiliadoByNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno) {
		return iAfiliadoDao.getIdAfiliadoByNombreCompleto(nombre, apellidoPaterno, apellidoMaterno);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Afiliado> findAll(Pageable pageable) {
		return iAfiliadoDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void updateEstatusbyIdCuenta(Long id){
		iAfiliadoDao.updateEstatusbyIdCuenta(id);
	}

	@Override
	@Transactional
	public void updateEstatusAfiliadoById(Long id) {
		iAfiliadoDao.updateEstatusAfiliadoById(id);
	}
	
	@Override
	public Integer getTotalAfiliados() {
		return iAfiliadoDao.getTotalAfiliados();
	}


}
