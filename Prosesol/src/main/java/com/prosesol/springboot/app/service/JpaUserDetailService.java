package com.prosesol.springboot.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Role;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.dao.IUsuarioDao;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Error login: no existe el usuario " + username);
			throw new UsernameNotFoundException("Usuario no existe con este nombre: " + username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (Role roles : usuario.getRoles()) {
			logger.info("Role ".concat(roles.getRoleName()));
			authorities.add(new SimpleGrantedAuthority(roles.getRoleName()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("Error login: usuario " + username + " no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: usuario " + username + " no tiene roles asignados");
		}
		
		return new User(username, usuario.getPassword(), usuario.getEstatus(), true, true, true, authorities);
	}

}
