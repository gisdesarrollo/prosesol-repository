package com.prosesol.springboot.app.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Adjunto;

public interface IAdjuntoDao extends CrudRepository<Adjunto, Long> {

	@Query(value = "select a.* from cat_adjuntos a where id_correo = ?", nativeQuery = true)
	public List<Adjunto> getAdjuntoCorreo(Long id);
	
}
