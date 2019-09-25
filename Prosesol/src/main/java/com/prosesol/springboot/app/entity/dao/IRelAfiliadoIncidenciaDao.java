package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRelAfiliadoIncidenciaDao extends CrudRepository<RelAfiliadoIncidencia, Long>{

	@Query(value = "select r.id_incidencia, r.id_afiliado, r.fecha, r.costo from rel_afiliados_incidencias r where r.id_incidencia = ?1 order by r.id_beneficio", nativeQuery = true)
	public List<RelAfiliadoIncidencia> getRelAfiliadoIncidenciaByIdIncidencia(Long id);


}
