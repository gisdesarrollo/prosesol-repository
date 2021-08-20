package com.prosesol.springboot.app.entity.dao;


import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRelAfiliadoMoneygramDao extends CrudRepository<RelAfiliadoMoneygram, Long> {

    @Query("select r from RelAfiliadoMoneygram r, Afiliado a where r.afiliado.id = a.id ")
    public List<RelAfiliadoMoneygram> getAfiliadoMoneygramList(Afiliado afiliado);

}
