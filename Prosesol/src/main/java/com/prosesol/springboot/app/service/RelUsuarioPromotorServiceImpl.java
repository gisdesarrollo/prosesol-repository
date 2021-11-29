package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.dao.IRelUsuarioPromotorDao;
import com.prosesol.springboot.app.entity.rel.RelUsuarioPromotor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelUsuarioPromotorServiceImpl implements IRelUsuarioPromotorService{

    @Autowired
    private IRelUsuarioPromotorDao relUsuarioPromotorDao;

    @Override
    public RelUsuarioPromotor getPromotorByIdUsuario(Usuario usuario) {
        return relUsuarioPromotorDao.getPromotorByIdUsuario(usuario);
    }

    @Override
    public void save(RelUsuarioPromotor relUsuarioPromotor) {
        relUsuarioPromotorDao.save(relUsuarioPromotor);
    }
}
