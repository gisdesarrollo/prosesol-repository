package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import com.prosesol.springboot.app.entity.custom.IncidenciaCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Incidencia;
import org.springframework.data.repository.query.Param;

public interface IIncidenciaDao extends CrudRepository<Incidencia, Long>{

	@Query(value = "select distinct i.id_incidencia, i.nombre_afiliado, " +
					"i.fecha, i.hora, i.localizacion, i.tipo_incidencia, i.detalle, i.proveedor, " +
					"i.estatus, i.fecha_creacion from incidencias i, " + 
					"afiliados a, servicios s, usuarios u " +
					"where a.id_servicio = s.id_servicio " + 
					"and s.id_centro_contacto = u.id_centro_contacto " + 
					"and u.username like %?1", nativeQuery = true)
	public List<Incidencia> getIncidenciasByUserName(String username);

	@Query("select new com.prosesol.springboot.app.entity.custom.IncidenciaCustom(i.id, i.fecha, i.detalle, i.estatus, b.nombre) " +
		   "from RelAfiliadoIncidencia r " +
	       "left join r.incidencia i " +
		   "left join r.beneficio b " +
			"where r.afiliado.id = ?1")
	public List<IncidenciaCustom> getHistorialIncidenciaByIdAfiliado(@Param("id") Long id);
}
