package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long>{

	public Usuario findByUsername(String username);
	
	
}
