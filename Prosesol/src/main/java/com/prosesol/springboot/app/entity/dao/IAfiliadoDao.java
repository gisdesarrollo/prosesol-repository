package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IAfiliadoDao extends CrudRepository<Afiliado, Long>{
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and  a.is_beneficiario = true and b.afiliado_id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long id);
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.afiliado_id_afiliado and a.is_beneficiario = false and b.id_beneficiario = ?1", nativeQuery = true)
	public Afiliado getAfiliadoAssignedBeneficiario(Long id);
	
}
