package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import com.prosesol.springboot.app.entity.custom.IncidenciaCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Incidencia;
import org.springframework.data.repository.query.Param;

public interface IIncidenciaDao extends CrudRepository<Incidencia, Long>{

	@Query(value = "select i.id_incidencia, i.nombre_afiliado, " +
					"i.fecha, i.hora, i.localizacion, i.tipo_incidencia, i.detalle, i.proveedor, " +
					"i.estatus, i.fecha_creacion from incidencias i, " + 
					"rel_afiliados_incidencias rai, afiliados a, servicios s, usuarios u " +
					"where i.id_incidencia = rai.id_incidencia " +
					"and a.id_afiliado = rai.id_afiliado " +
					"and a.id_servicio = s.id_servicio " +
					"and s.id_centro_contacto = u.id_centro_contacto " +
					"and u.id_usuario = ?1 " +
					"group by i.id_incidencia " +
					"order by i.id_incidencia", nativeQuery = true)
	public List<Incidencia> getIncidenciasByUserId(Long id);

	@Query("select new com.prosesol.springboot.app.entity.custom.IncidenciaCustom(i.id, i.fecha, i.detalle, i.estatus, b.nombre) " +
		   "from RelAfiliadoIncidenciaBeneficio r " +
	       "left join r.incidencia i " +
		   "left join r.beneficio b " +
			"where r.afiliado.id = ?1")
	public List<IncidenciaCustom> getHistorialIncidenciaByIdAfiliado(@Param("id") Long id);
}
