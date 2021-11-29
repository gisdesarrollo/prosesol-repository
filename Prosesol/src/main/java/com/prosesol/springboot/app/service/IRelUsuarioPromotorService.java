package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.rel.RelUsuarioPromotor;

public interface IRelUsuarioPromotorService {

    public RelUsuarioPromotor getPromotorByIdUsuario(Usuario usuario);

    public void save(RelUsuarioPromotor relUsuarioPromotor);
}
