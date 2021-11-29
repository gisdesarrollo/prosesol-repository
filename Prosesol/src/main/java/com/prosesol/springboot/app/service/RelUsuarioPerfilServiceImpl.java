package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.dao.IRelUsuarioPerfilDao;
import com.prosesol.springboot.app.entity.rel.RelUsuarioPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelUsuarioPerfilServiceImpl implements IRelUsuarioPerfilService{

    @Autowired
    private IRelUsuarioPerfilDao usuarioPerfilDao;

    @Override
    public void save(RelUsuarioPerfil relUsuarioPerfil) {
        usuarioPerfilDao.save(relUsuarioPerfil);
    }

    @Override
    public RelUsuarioPerfil findById(Long id) {
        return usuarioPerfilDao.findById(id).orElse(null);
    }
}
