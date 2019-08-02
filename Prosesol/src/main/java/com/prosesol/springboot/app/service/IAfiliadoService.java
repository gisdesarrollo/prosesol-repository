package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.util.Paises;

public interface IAfiliadoService {

	public List<Afiliado> findAll();
	
	public Page<Afiliado> findAll(Pageable pageable);
	
	public void save(Afiliado afiliado);
	
	public void delete(Long id);
	
	public Afiliado findById(Long id);
	
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long idAfiliado);
	
	public void insertBeneficiarioUsingJpa(Afiliado beneficiario, Long id);
	
	public Long getIdAfiliadoByNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno);
	
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id);
	
	public List<String> getAllEstados();
	
	public List<Paises> getAllPaises();
	
	public String[] getVariablesAfiliado();	
	
	public Long getIdAfiliadoByRfc(String rfc);
	
	public List<AfiliadoCustom> getAfiliadoByParams(String[] campos, Long idCcUsuario);
}
