package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.dao.IRelAfiliadoMoneygramDao;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelAfiliadoMoneygramServiceImpl implements IRelAfiliadoMoneygramService{

    @Autowired
    private IRelAfiliadoMoneygramDao relAfiliadoMoneygramDao;


    @Override
    public List<RelAfiliadoMoneygram> getAfiliadoMoneygramList(Afiliado afiliado) {
        return relAfiliadoMoneygramDao.getAfiliadoMoneygramList(afiliado);
    }

    @Override
    @Transactional
    public void save(RelAfiliadoMoneygram relAfiliadoMoneygram) {
        relAfiliadoMoneygramDao.save(relAfiliadoMoneygram);
    }


}
