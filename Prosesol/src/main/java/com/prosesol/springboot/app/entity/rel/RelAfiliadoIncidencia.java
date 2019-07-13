package com.prosesol.springboot.app.entity.rel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Incidencia;

@Entity
@Table(name = "rel_afiliados_incidencias")
public class RelAfiliadoIncidencia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_afiliado")
	private Afiliado afiliado;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_incidencia")
	private Incidencia incidencia;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof RelAfiliadoIncidencia))return true;
		
		RelAfiliadoIncidencia relAfiliadoIncidencia = (RelAfiliadoIncidencia)obj;
		
		return Objects.equals(afiliado.getNombre(), this.afiliado.getNombre()) &&
			   Objects.equals(incidencia.getNombreAfiliado(), this.incidencia.getNombreAfiliado())&&
			   Objects.equals(fecha, relAfiliadoIncidencia.fecha);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(afiliado.getNombre(), incidencia.getNombreAfiliado(), fecha);
	}


}
