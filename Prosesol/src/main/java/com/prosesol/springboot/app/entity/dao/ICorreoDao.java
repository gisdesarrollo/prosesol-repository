package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Correo;

public interface ICorreoDao extends CrudRepository<Correo, Long>{

	@Query("select c from Correo c where tipoCorreo = ?1")
	public Correo getTemplateCorreByName(String name);
	
}
