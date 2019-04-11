package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "servicios")
public class Servicio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Proporcione un nombre para la membresía")
	@Column(name = "nombre")
	private String nombre;
	
	@NotEmpty(message = "Proporcione el tipo de membresía")
	@Column(name = "tipoMembresia")
	private String tipoMembresia;
	
	@Column(name = "costo")
	private BigDecimal costo;
	
	@Column(name = "periodicidad")
	private String periodicidad;
	
	@Column(name = "estatus")
	private String estatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoMembresia() {
		return tipoMembresia;
	}

	public void setTipoMembresia(String tipoMembresia) {
		this.tipoMembresia = tipoMembresia;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
}
