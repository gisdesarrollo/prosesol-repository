package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Usuario;
import com.prosesol.springboot.app.entity.rel.RelUsuarioPromotor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IRelUsuarioPromotorDao extends CrudRepository<RelUsuarioPromotor, Long> {

    @Query("select rus from RelUsuarioPromotor rus where rus.usuario = :usuario")
    public RelUsuarioPromotor getPromotorByIdUsuario(@Param("usuario") Usuario usuario);
}
