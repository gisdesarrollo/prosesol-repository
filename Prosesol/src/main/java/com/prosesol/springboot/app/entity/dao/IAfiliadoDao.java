package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Afiliado;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAfiliadoDao extends DataTablesRepository<Afiliado, Long>{
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and  a.is_beneficiario = true and b.id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getBeneficiarioByIdByIsBeneficiario(Long id);
	
	@Query(value = "select a.* from afiliados a, rel_afiliados_beneficiarios b where a.id_afiliado = b.id_beneficiario and a.is_beneficiario = true and b.id_afiliado = ?1", nativeQuery = true)
	public List<Afiliado> getAfiliadoAssignedBeneficiario(Long id);
	
	@Query("select a from Afiliado a where a.rfc like %:rfc%")
	public Afiliado getAfiliadoByRfc(@Param("rfc")String rfc);
	
	@Query("select a.id from Afiliado a where a.servicio = ?1")
	public Long getAfiliadoByIdServicio(@Param("id") Long idServicio);
	
	@Query(value = "select a.id_afiliado from afiliados a where a.nombre like %?1% and a.apellido_paterno like ?2 and a.apellido_materno like ?3", nativeQuery = true)
	public Long getIdAfiliadoByNombreCompleto(String nombre, String apellidoPaterno, String apellidoMaterno);

	@Query(value = "select a.* from afiliados a where a.nombre like %?1% and a.apellido_paterno like %?2% " +
                   "and a.apellido_materno like %?3%", nativeQuery = true)
	public List<Afiliado> getAfiliadoBySearchNombreCompleto(String nombre, String apellidoPaterno,
															String apellidoMaterno);

	@Query(value = "select * from afiliados a where fecha_corte = ?1 and is_beneficiario = false", nativeQuery = true)
	public List<Afiliado> getAfiliadosByFechaCorte(String fecha);

	@Query(value = "select * from afiliados where fecha_corte <= curdate() and is_beneficiario = false and saldo_corte > 0", nativeQuery = true)
	public List<Afiliado> getAfiliadosPagoPendiente();

	@Modifying
	@Query("update Afiliado a set a.estatus = 2 where a.cuenta.id = :id")
	public void updateEstatusbyIdCuenta(@Param("id")Long id);

	@Modifying
	@Query("update Afiliado a set a.estatus = 1 where a.id = :id")
	public void updateEstatusAfiliadoById(@Param("id") Long id);
	
	@Query("select count(a) from Afiliado a")
	public Integer getTotalAfiliados();
}
