package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Perfil;

public interface IPerfilDao extends CrudRepository<Perfil, Long>{
	
	@Query(value = "select p.* from perfiles p, rel_usuarios_perfiles b, usuarios u where u.id_usuario = b.id_usuario and p.id_perfil = b.id_perfil and u.id_usuario = ?1", nativeQuery = true)
	public Perfil findPerfilByUsuario(Long id);
}
