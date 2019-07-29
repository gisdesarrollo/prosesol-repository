package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	public Usuario findByUsername(String username);
	
	@Modifying
	@Query(value = "update rel_usuarios_perfiles r set r.id_perfil = ?1 where r.id_usuario = ?2", nativeQuery = true)
	public void updateRelUsuarioPerfil(Long idPerfil, Long idUsuario);
	
}
