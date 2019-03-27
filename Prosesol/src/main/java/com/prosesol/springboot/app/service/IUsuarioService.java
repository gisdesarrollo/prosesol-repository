package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Usuario;

public interface IUsuarioService {

	public List<Usuario> findAll();
	
	public void save(Usuario usuario);
	
	public void delete(Long id);
	
	public Usuario findById(Long id);
	
}
