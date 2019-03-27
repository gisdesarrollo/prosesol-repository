package com.prosesol.springboot.app.service;

import java.util.List;

import com.prosesol.springboot.app.entity.Role;

public interface IRoleService {

	public List<Role> findAll();
	
	public void save(Role role);
	
	public void delete(Long id);
	
	public Role findById(Long id);
	
	
}
