package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "servicios")
public class Servicio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servicio", unique = true, nullable = false)
	private Long id;
	
	@NotEmpty(message = "Proporcione el nombre del servicio")
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "notas")
	private String nota;
		
	@NotNull(message = "Proporcione el costo del servicio")
	@Column(name = "costo")
	private Double costo;
	
	@Column(name = "inscripcion")
	private String inscripcion;
	
	@Column(name = "estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "servicio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Afiliado> afiliado;
	
	public Servicio() {
		afiliado = new ArrayList<Afiliado>();
	}
	
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

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public List<Afiliado> getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(List<Afiliado> afiliado) {
		this.afiliado = afiliado;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public String getInscripcion() {
		return inscripcion;
	}

	public void setInscripcion(String inscripcion) {
		this.inscripcion = inscripcion;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	
}
