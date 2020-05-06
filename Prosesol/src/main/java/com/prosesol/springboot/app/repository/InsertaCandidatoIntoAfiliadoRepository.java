package com.prosesol.springboot.app.repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InsertaCandidatoIntoAfiliadoRepository {

    private final static Log LOG = LogFactory.getLog(InsertaCandidatoIntoAfiliadoRepository.class);

    @Value("${sql.sp.activar.candidato}")
	private String SQL_SP_AC;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public boolean insertAfiliado(Long idCandidato, String clave) {

        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(SQL_SP_AC);

        //Registrar los parámetros de entrada
        storedProcedureQuery.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(3, boolean.class, ParameterMode.OUT);

        // Configuramos el valor de entrada
        storedProcedureQuery.setParameter(1, idCandidato);
        storedProcedureQuery.setParameter(2, clave);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        boolean mensaje = (boolean) storedProcedureQuery.getOutputParameterValue(3);

        LOG.info(mensaje);


        return mensaje;


    }
}
