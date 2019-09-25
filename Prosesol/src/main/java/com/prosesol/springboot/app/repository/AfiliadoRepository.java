package com.prosesol.springboot.app.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.util.AfiliadoAsistencia;

@Repository
public class AfiliadoRepository {

	protected static final Log LOG = LogFactory.getLog(AfiliadoRepository.class);

	@PersistenceContext
	public EntityManager em;

	@SuppressWarnings("unchecked")
	@Transactional
	public List<AfiliadoCustom> getAfiliadoByParams(String[] campos, Long idCcUsuario) {

		Afiliado afiliado = new Afiliado();

		List<AfiliadoCustom> afiliados = new ArrayList<AfiliadoCustom>();

		boolean where = true;
		boolean nombre = false;
		boolean apellidoPaterno = false;
		boolean apellidoMaterno = false;
		boolean rfc = false;
		boolean telefono = false;

		String query = "select new com.prosesol.springboot.app.entity.custom.AfiliadoCustom(a.id, a.nombre, "
				+ "a.apellidoPaterno, a.apellidoMaterno, a.telefonoFijo, a.telefonoMovil, "
				+ "a.clave, a.municipio, a.entidadFederativa, a.servicio, a.estatus) from Afiliado a "
				+ "left join a.servicio as s "
				+ "left join s.centroContacto as cc ";

		String nombreQuery = afiliado.getQuery(campos[0], where, AfiliadoAsistencia.NOMBRE, idCcUsuario);
		String apellidoPaternoQuery = afiliado.getQuery(campos[1], where, AfiliadoAsistencia.APELLIDO_PATERNO, idCcUsuario);
		String apellidoMaternoQuery = afiliado.getQuery(campos[2], where, AfiliadoAsistencia.APELLIDO_MATERNO, idCcUsuario);
		String rfcQuery = afiliado.getQuery(campos[3], where, AfiliadoAsistencia.RFC, idCcUsuario);
		String telefonoQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.TELEFONO, idCcUsuario);
		String claveQuery = afiliado.getQuery(campos[5], where, AfiliadoAsistencia.CLAVE, idCcUsuario);

		if (nombreQuery != null) {

			query += nombreQuery;
			nombre = true;

			where = false;
		}
		if (apellidoPaternoQuery != null) {

			if (nombre) {
				apellidoPaternoQuery = afiliado.getQuery(campos[1], where, AfiliadoAsistencia.APELLIDO_PATERNO, idCcUsuario);
				query += apellidoPaternoQuery;
			} else {
				query += apellidoPaternoQuery;
			}
			apellidoPaterno = true;
			where = false;
		}
		if (apellidoMaternoQuery != null) {

			if (nombre || apellidoMaterno) {
				apellidoMaternoQuery = afiliado.getQuery(campos[2], where, AfiliadoAsistencia.APELLIDO_MATERNO, idCcUsuario);
				query += apellidoMaternoQuery;
			} else {
				query += apellidoMaternoQuery;
			}

			apellidoMaterno = true;
			where = false;

		}
		if (rfcQuery != null) {

			if (nombre || apellidoPaterno || apellidoMaterno) {
				rfcQuery = afiliado.getQuery(campos[3], where, AfiliadoAsistencia.RFC, idCcUsuario);
				query += rfcQuery;
			} else {
				query += rfcQuery;
			}

			rfc = true;
			where = false;
		}
		if (telefonoQuery != null) {

			if (nombre || apellidoPaterno || apellidoMaterno || rfc) {
				telefonoQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.TELEFONO, idCcUsuario);
				query += telefonoQuery;
			} else {
				query += telefonoQuery;
			}

			telefono = true;
			where = false;
		}
		if (claveQuery != null) {

			if (nombre || apellidoPaterno || apellidoMaterno || rfc || telefono) {
				claveQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.CLAVE, idCcUsuario);
				query += claveQuery;
			} else {
				query += claveQuery;
			}
		}

		LOG.info(query);
		Query sql = em.createQuery(query);

		afiliados = sql.getResultList();

		return afiliados;
	}

}
