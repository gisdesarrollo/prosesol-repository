package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import com.prosesol.springboot.app.entity.Candidato;

public interface ICandidatoDao extends DataTablesRepository<Candidato, Long>{
	
	@Query(value = "select a.* from candidatos a where a.nombre like %?1% and a.apellido_paterno like %?2% " +
            "and a.apellido_materno like %?3%", nativeQuery = true)
	public List<Candidato> getCandidatoBySearchNombreCompleto(String nombre, String apellidoPaterno,
														String apellidoMaterno);
}
