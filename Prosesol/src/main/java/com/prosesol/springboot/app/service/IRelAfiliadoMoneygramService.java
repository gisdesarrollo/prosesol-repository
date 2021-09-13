package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;

import java.util.List;

public interface IRelAfiliadoMoneygramService {

    public List<RelAfiliadoMoneygram> getAfiliadoMoneygramList(Afiliado afiliado);

    public void save(RelAfiliadoMoneygram relAfiliadoMoneygram);

//    public List<RelAfiliadoMoneygram> getAfiliadosByIdPromotor(Long id);

    public List<RelAfiliadoMoneygram> findAll();
    
   	public String[] getVariablesRelAfiliadoMoneygram();
}
