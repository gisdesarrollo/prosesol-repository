package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.rel.RelUsuarioPerfil;
import org.springframework.data.repository.CrudRepository;

public interface IRelUsuarioPerfilDao extends CrudRepository<RelUsuarioPerfil, Long> {
}
