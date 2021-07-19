package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

public interface IRelServicioBeneficioDao extends CrudRepository<RelServicioBeneficio, Long>{

	@Query(value = "select r.id_servicio, r.id_beneficio, r.descripcion from rel_servicios_beneficios r where r.id_servicio = ?1 order by r.id_beneficio", nativeQuery = true)
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long id);
	
	@Modifying
	@Query(value = "delete from rel_servicios_beneficios where id_servicio = ?1 and id_beneficio = ?2", nativeQuery = true)
	public void deleteBeneficioByIdBeneficioAndIdServicio(Long idServicio, Long idBeneficio);
	
	@Query(value = "select r.id_servicio, r.id_beneficio, r.descripcion from rel_servicios_beneficios r, afiliados a where r.id_servicio = a.id_servicio and a.id_afiliado = ?1 order by r.id_beneficio", nativeQuery = true)
	public List<RelServicioBeneficio> getBeneficioByIdAfiliado(Long idAfiliado);
	
	@Modifying
	@Query("update RelServicioBeneficio r set r.descripcion = null where id_beneficio = ?1")
	public void removeBeneficiobyIdBeneficio(Long idBeneficio);
	
	@Query(value="select r.id_servicio, r.id_beneficio, r.descripcion from rel_servicios_beneficios r where r.id_servicio = ?1 and r.id_beneficio= ?2", nativeQuery = true)
	public RelServicioBeneficio getRelServicioBeneficioByIdServcioAndIdBeneficio(Long idServicio,Long idBeneficio); 
	
	@Modifying
	@Query(value="delete from rel_servicios_beneficios where id_servicio = ?1 ", nativeQuery = true)
	public void deleteAllRelServicioBeneficioByIdServicio(Long idServicio);
	
}
