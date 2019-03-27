package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Role;

public interface IRoleDao extends CrudRepository<Role, Long>{
	
	@Query(value = "insert into roles (id_user, role_name) values(?1, ?2)", nativeQuery = true)
	public void insertarRole(Long id, String roleName);
	
}
