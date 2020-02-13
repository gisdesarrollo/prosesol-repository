package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IRelAfiliadoIncidenciaBeneficioDao extends CrudRepository<RelAfiliadoIncidenciaBeneficio, Long> {
	
	@Modifying 
	@Query(value="delete from rel_afiliados_incidencias where id_incidencia=?1 ",nativeQuery=true)
	public void deleteRelAfiliadoIncidenciaById(Long id);
	
	@Modifying 
	@Query(value="delete from rel_afiliados_incidencias where id_incidencia=?1 and id_beneficio=?2 ",nativeQuery=true)
	public void deleteRelAfiliadoIncidenciaByIdIncidenciaAndIdBeneneficio(Long idIncidencia,Long idBeneficio);
	
	@Modifying 
	@Query(value="update rel_afiliados_incidencias set id_beneficio=?1 , fecha=?2 where id_incidencia=?3",nativeQuery=true)
	public void updateRelAfiliadoIncidencia(String beneficio,Date fecha,Long idIncidencia);
	
}
