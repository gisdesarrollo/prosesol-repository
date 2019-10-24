package com.prosesol.springboot.app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TempAfiliadoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private String queryInsert = "insert into temp_afiliados select id_afiliado, "
            + "estatus, curdate() from afiliados where id_cta_comercial = ?1";

    private String queryDelete = "delete from temp_afiliados where fecha = curdate()";

    private String queryUpdate = "update afiliados a join temp_afiliados ta on(a.id_afiliado = ta.id_afiliado)" +
            "set a.estatus = true where ta.fecha = curdate()";

    @Transactional
    public void insertAfiliadosOnTemp(Long id){
        entityManager.createNativeQuery(queryInsert)
                .setParameter(1, id)
                .executeUpdate();
    }

    @Transactional
    public void updateAfiliadosByAfiliadosTemp(){
        entityManager.createNativeQuery(queryUpdate)
                .executeUpdate();
    }

    @Transactional
    public void deleteAfiliadosOnTemp(){
        entityManager.createNativeQuery(queryDelete)
                .executeUpdate();
    }

}
