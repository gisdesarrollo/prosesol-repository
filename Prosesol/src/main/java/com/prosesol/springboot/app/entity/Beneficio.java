package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Set;

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

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

@Table(name = "beneficios")
@Entity
public class Beneficio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_beneficio", unique = true, nullable = false)
	private Long id;

	@NotEmpty(message = "Proporcione el nombre del beneficio")
	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	@OneToMany(mappedBy = "beneficio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<RelServicioBeneficio> relServicioBeneficio;
	
	@OneToMany(mappedBy = "beneficio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<RelAfiliadoIncidencia> relAfiliadoIncidencia;

	public Beneficio() {
	}

	public Beneficio(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<RelServicioBeneficio> getRelServicioBeneficio() {
		return relServicioBeneficio;
	}

	public void setRelServicioBeneficio(Set<RelServicioBeneficio> relServicioBeneficio) {
		this.relServicioBeneficio = relServicioBeneficio;
	}

	public Set<RelAfiliadoIncidencia> getRelAfiliadoIncidencia() {
		return relAfiliadoIncidencia;
	}

	public void setRelAfiliadoIncidencia(Set<RelAfiliadoIncidencia> relAfiliadoIncidencia) {
		this.relAfiliadoIncidencia = relAfiliadoIncidencia;
	}
	
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("Id: [").append(id).append("] ").append("Nombre: [").append(nombre).append("] ")
				.append("Descripción: [").append(descripcion).append("] ");

		return buffer.toString();

	}

}
