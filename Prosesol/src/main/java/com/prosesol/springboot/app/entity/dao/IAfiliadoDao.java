package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prosesol.springboot.app.entity.Afiliado;

public interface IAfiliadoDao extends DataTablesRepository<Afiliado, Long>{
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and  a.is_beneficiario = true and b.id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long id);
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and a.is_beneficiario = true and b.id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id);
	
	@Query("select a.id from Afiliado a where a.rfc like %:rfc%")
	public Long getIdAfiliadoByRfc(@Param("rfc")String rfc);
	
	@Query("select a.id from Afiliado a where a.servicio = ?1")
	public Long getAfiliadoByIdServicio(@Param("id") Long idServicio);
	
	@Query(value = "select a.id_afiliado from afiliados a where a.nombre like %?1% and a.apellido_paterno like ?2 and a.apellido_materno like ?3", nativeQuery = true)
	public Long getIdAfiliadoByNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno);

	@Query(value = "select a.* from afiliados a where a.nombre like %?1% and a.apellido_paterno like %?2% " +
                   "and a.apellido_materno like %?3%", nativeQuery = true)
	public List<Afiliado> getAfiliadoBySearchNombreCompleto(String nombre, String apellidoPaterno,
															String apellidoMaterno);
	
}
