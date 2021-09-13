package com.prosesol.springboot.app.entity.dao;


import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IRelAfiliadoMoneygramDao extends CrudRepository<RelAfiliadoMoneygram, Long> {

    @Query("select r from RelAfiliadoMoneygram r, Afiliado a where r.afiliado.id = a.id ")
    public List<RelAfiliadoMoneygram> getAfiliadoMoneygramList(Afiliado afiliado);

    @Query("select r from RelAfiliadoMoneygram r, Afiliado a, Promotor p where r.afiliado = a " +
            "and a.promotor = p and p.nombre like %:username%")
    public List<RelAfiliadoMoneygram> getAfiliadosByUsername(@Param("username") String username);
}
