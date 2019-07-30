package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Incidencia;

public interface IIncidenciaDao extends CrudRepository<Incidencia, Long>{

	@Query(value = "select distinct i.id_incidencia, i.nombre_afiliado, " +
					"i.fecha, i.hora, i.localizacion, i.tipo_incidencia, i.detalle, i.proveedor, " +
					"i.estatus from incidencias i, " + 
					"afiliados a, servicios s, usuarios u " +
					"where a.id_servicio = s.id_servicio " + 
					"and s.id_centro_contacto = u.id_centro_contacto " + 
					"and u.username like %?1", nativeQuery = true)
	public List<Incidencia> getIncidenciasByUserName(String username);
	
}
