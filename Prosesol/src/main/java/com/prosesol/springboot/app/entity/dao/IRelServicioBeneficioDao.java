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
	@Query(value = "update rel_servicios_beneficios set id_beneficio = ?2, costo = ?3, descripcion = ?4 where id_servicio = ?1 and id_servicio_beneficio = ?5", nativeQuery = true)
	public void updateRelServicioBeneficio(Long idServicio, Long idBeneficio, Double costo, String descripcion, Long idServicioBeneficio);
}
