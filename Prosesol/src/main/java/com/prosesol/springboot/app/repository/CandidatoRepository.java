package com.prosesol.springboot.app.repository;

import com.prosesol.springboot.app.entity.Candidato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CandidatoRepository {

    @PersistenceContext
    private EntityManager em;

    private String query = "insert into afiliados\n" +
            "select \n" +
            "0,\n" +
            "apellido_materno,\n" +
            "apellido_paterno,\n" +
            "clave,\n" +
            "codigo_postal,\n" +
            "comentarios,\n" +
            "curp,\n" +
            "direccion,\n" +
            "email,\n" +
            "entidad_federativa,\n" +
            "estado_civil,\n" +
            "estatus,\n" +
            "fecha_afiliacion,\n" +
            "fecha_alta,\n" +
            "fecha_corte,\n" +
            "fecha_nacimiento,\n" +
            "infonavit,\n" +
            "inscripcion,\n" +
            "is_beneficiario,\n" +
            "lugar_nacimiento,\n" +
            "municipio,\n" +
            "nombre,\n" +
            "nss,\n" +
            "dependientes,\n" +
            "numero_infonavit,\n" +
            "ocupacion,\n" +
            "pais,\n" +
            "rfc,\n" +
            "saldo_acumulado,\n" +
            "saldo_corte,\n" +
            "sexo,\n" +
            "telefono_fijo,\n" +
            "telefono_movil,\n" +
            "id_cta_comercial,\n" +
            "id_periodicidad,\n" +
            "id_promotor,\n" +
            "id_servicio,\n" +
            "is_inscripcion\n" +
            "from candidatos where id_candidato = ?1";

    @Transactional
    public void insertCandidatoIntoAfiliado(Candidato candidato){
        em.createNativeQuery(query)
                .setParameter(1, candidato.getId())
                .executeUpdate();
    }

}
