package com.prosesol.springboot.app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RelAfiliadoIncidenciaBeneficioRepository {

    @PersistenceContext
    private EntityManager em;

    private String query = "select new com.prosesol.springboot.app.entity.custom.RelAfiliadoIncidenciaBeneficioCustom"
                + "(rai.incidencia.id, rai.afiliado.id, rai.beneficio.id, rai.fecha) from RelAfiliadoIncidenciaBeneficio rai "
                + "where rai.incidencia.id = ?1";

    @Transactional
    public List getRelAfiliadoIncidenciaBeneficioByIdIncidencia(Long id){
        return em.createQuery(query)
                 .setParameter(1, id)
                .getResultList();
    }

}
