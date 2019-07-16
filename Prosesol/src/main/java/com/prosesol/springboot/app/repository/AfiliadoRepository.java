package com.prosesol.springboot.app.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.custom.AfiliadoCustom;
import com.prosesol.springboot.app.util.AfiliadoAsistencia;

@Repository
public class AfiliadoRepository {

	@PersistenceContext
	public EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<AfiliadoCustom> getAfiliadoByParams(String[] campos){		
		
		Afiliado afiliado = new Afiliado();
		
		List<AfiliadoCustom> afiliados = new ArrayList<AfiliadoCustom>();
		
		boolean where = true;
		boolean nombre = false;
		boolean apellidoPaterno = false;
		boolean apellidoMaterno = false;
		boolean rfc = false;
		boolean telefono = false;
		
		String query = "select new com.prosesol.springboot.app.entity.custom.AfiliadoCustom(a.id, a.nombre, a.apellidoPaterno, a.apellidoMaterno, a.telefonoFijo, a.telefonoMovil, a.clave, a.municipio, a.entidadFederativa) from Afiliado a ";
		
		String nombreQuery = afiliado.getQuery(campos[0], where, AfiliadoAsistencia.NOMBRE);
		String apellidoPaternoQuery = afiliado.getQuery(campos[1], where, AfiliadoAsistencia.APELLIDO_PATERNO);
		String apellidoMaternoQuery = afiliado.getQuery(campos[2], where, AfiliadoAsistencia.APELLIDO_MATERNO);
		String rfcQuery = afiliado.getQuery(campos[3], where, AfiliadoAsistencia.RFC);
		String telefonoQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.TELEFONO);
		String claveQuery = afiliado.getQuery(campos[5], where, AfiliadoAsistencia.CLAVE);		
		
		
		if(nombreQuery != null) {
			
			query += nombreQuery;
			nombre = true;
			
			where = false;
		}if(apellidoPaternoQuery != null) {
			
			if(nombre) {
				apellidoPaternoQuery = afiliado.getQuery(campos[1], where, AfiliadoAsistencia.APELLIDO_PATERNO);
				query += apellidoPaternoQuery;
			}else {
				query += apellidoPaternoQuery;
			}
			apellidoPaterno = true;
			where = false;
		}if(apellidoMaternoQuery != null) {
			
			if(nombre || apellidoPaterno) {
				apellidoMaternoQuery = afiliado.getQuery(campos[2], where, AfiliadoAsistencia.APELLIDO_MATERNO);
				query += apellidoMaternoQuery;
			}else {
				query += apellidoPaternoQuery;
			}
			
			apellidoMaterno = true;
			where = false;
			
		}if(rfcQuery != null) {
			
			if(nombre || apellidoPaterno || apellidoMaterno) {
				rfcQuery = afiliado.getQuery(campos[3], where, AfiliadoAsistencia.RFC);
				query += rfcQuery;
			}else {
				query += rfcQuery;
			}
			
			rfc = true;
			where = false;
		}if(telefonoQuery != null) {
			
			if(nombre || apellidoPaterno || apellidoMaterno || rfc) {
				telefonoQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.TELEFONO);
				query += telefonoQuery;
			}else {
				query += telefonoQuery;
			}
			
			telefono = true;
			where = false;
		}if(claveQuery != null) {
			
			if(nombre || apellidoPaterno || apellidoMaterno || rfc || telefono) {
				claveQuery = afiliado.getQuery(campos[4], where, AfiliadoAsistencia.CLAVE);
				query += claveQuery;
			}else {
				query += claveQuery;
			}
		}
		
		System.out.println(query);
		
		Query sql = em.createQuery(query);
		
		afiliados = sql.getResultList();
		
		for(AfiliadoCustom nuevoAfiliado : afiliados) {
			System.out.println(nuevoAfiliado.toString());
		}
		
		return afiliados;
	}
	
}
