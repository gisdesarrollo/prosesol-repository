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

    private String deleteAfiliadosTemp = "delete from temp_afiliados";

    /**
     * Inserta en la tabla temporal de afiliados, el respaldo de los datos de
     * la carga masiva Vigor
     * @param id
     */

    @Transactional
    public void insertAfiliadosOnTemp(Long id){
        entityManager.createNativeQuery(queryInsert)
                .setParameter(1, id)
                .executeUpdate();
    }

    /**
     * Si ocurre un error, se actualizarán los estatus de los afiliados incativos
     * durante la carga de vigor solamente el día que fueron solicitados
     */

    @Transactional
    public void updateAfiliadosByAfiliadosTemp(){
        entityManager.createNativeQuery(queryUpdate)
                .executeUpdate();
    }

    /**
     * Se borrarán los registros de la tabla temporal de afiliados para
     * no sobrecargar la tabla de tantos datos
     */

    @Transactional
    public void deleteAfiliadosOnTempByFechaActual(){
        entityManager.createNativeQuery(queryDelete)
                .executeUpdate();
    }

    /**
     * Cada semana, se depurará la tabla temporal de afiliados
     */

    @Transactional
    public void deleleteAfiliadosTemp(){
        entityManager.createNativeQuery(deleteAfiliadosTemp)
                .executeUpdate();
    }
}
