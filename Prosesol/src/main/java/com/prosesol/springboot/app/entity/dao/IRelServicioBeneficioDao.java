package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

public interface IRelServicioBeneficioDao extends CrudRepository<RelServicioBeneficio, Long>{

	@Query(value = "select r.id_servicio, r.id_beneficio, r.descripcion, r.costo from rel_servicios_beneficios r where r.id_servicio = ?1", nativeQuery = true)
	public List<RelServicioBeneficio> getRelServicioBeneficioByIdServicio(Long id);
	
	@Modifying
	@Query(value = "delete from rel_servicios_beneficios where id_servicio = ?1 and id_beneficio = ?2", nativeQuery = true)
	public void deleteBeneficioByIdBeneficioAndIdServicio(Long idServicio, Long idBeneficio);
}
