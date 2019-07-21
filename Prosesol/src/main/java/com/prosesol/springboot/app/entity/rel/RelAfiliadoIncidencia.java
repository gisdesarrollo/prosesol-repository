package com.prosesol.springboot.app.entity.rel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.composite.id.RelAfiliadoIncidenciaId;

@Entity
@Table(name = "rel_afiliados_incidencias")
@IdClass(RelAfiliadoIncidenciaId.class)
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
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_beneficio")
	private Beneficio beneficio;
	
	@Column(name = "costo")
	private Double costo;
	
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	public Afiliado getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(Afiliado afiliado) {
		this.afiliado = afiliado;
	}

	public Beneficio getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(Beneficio beneficio) {
		this.beneficio = beneficio;
	}

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
