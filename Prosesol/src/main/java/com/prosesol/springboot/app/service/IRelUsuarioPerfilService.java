package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.rel.RelUsuarioPerfil;

public interface IRelUsuarioPerfilService {

    public void save(RelUsuarioPerfil relUsuarioPerfil);

    public RelUsuarioPerfil findById(Long id);
}
