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

import com.prosesol.springboot.app.entity.Perfil;
import com.prosesol.springboot.app.entity.Role;
import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.dao.IPerfilDao;
import com.prosesol.springboot.app.entity.dao.IUsuarioDao;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	IPerfilDao perfilDao;

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		try {
			if (usuario == null) {
				LOG.error("Error login: no existe el usuario " + username);
				throw new UsernameNotFoundException("Usuario no existe con este nombre: " + username);
			}

			Perfil perfil = perfilDao.findPerfilByUsuario(usuario.getId());

			LOG.info("Role ".concat(perfil.getRoles().getNombre()));
			authorities.add(new SimpleGrantedAuthority(perfil.getRoles().getNombre()));

			LOG.info("authorities: " + authorities);

			if (authorities.isEmpty()) {
				LOG.error("Error login: usuario " + username + " no tiene roles asignados");
				throw new UsernameNotFoundException("Error login: usuario " + username + " no tiene roles asignados");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return new User(username, usuario.getPassword(), usuario.getEstatus(), true, true, true, authorities);
	}

}
