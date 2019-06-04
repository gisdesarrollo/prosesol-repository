package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IAfiliadoDao extends CrudRepository<Afiliado, Long>{
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and  a.is_beneficiario = true and b.afiliado_id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long id);
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and a.is_beneficiario = true and b.afiliado_id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id);
	
	@Query("select a.id from Afiliado a where a.rfc like %:rfc%")
	public Long getIdAfiliadoByRfc(@Param("rfc")String rfc);
	
}
