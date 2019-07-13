package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Beneficio;

public interface IBeneficioDao extends CrudRepository<Beneficio, Long>{

	@Query(value= "select b.id_beneficio, b.nombre, b.descripcion from beneficios b, servicios s, rel_servicios_beneficios rsb where s.id_servicio = rsb.id_servicio and b.id_beneficio = rsb.id_beneficio and s.id_servicio = ?1", nativeQuery = true)
	public List<Beneficio> getBeneficiosByIdServicio(Long id);
}
