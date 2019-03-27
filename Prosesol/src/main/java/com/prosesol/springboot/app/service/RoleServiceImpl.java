package com.prosesol.springboot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Role;
import com.prosesol.springboot.app.entity.dao.IRoleDao;

@Service
public class RoleServiceImpl implements IRoleService{

	@Autowired
	private IRoleDao roleDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Role> findAll() {	
		return (List<Role>)roleDao.findAll();
	}

	@Override
	@Transactional
	public void save(Role role) {
		roleDao.save(role);		
	}

	@Override
	@Transactional
	public void delete(Long id) {
		roleDao.deleteById(id);		
	}

	@Override
	@Transactional(readOnly = true)
	public Role findById(Long id) {
		return roleDao.findById(id).orElse(null);
	}


}
