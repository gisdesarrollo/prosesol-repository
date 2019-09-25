package com.prosesol.springboot.app.entity;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;
import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Table(name = "beneficios")
@Entity
public class
Beneficio implements Serializable {

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
	private Set<RelAfiliadoIncidenciaBeneficio> relAfiliadoIncidenciaBeneficio;

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

	public Set<RelAfiliadoIncidenciaBeneficio> getRelAfiliadoIncidenciaBeneficio() {
		return relAfiliadoIncidenciaBeneficio;
	}

	public void setRelAfiliadoIncidencia(Set<RelAfiliadoIncidenciaBeneficio> relAfiliadoIncidenciaBeneficio) {
		this.relAfiliadoIncidenciaBeneficio = relAfiliadoIncidenciaBeneficio;
	}
	
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("Id: [").append(id).append("] ").append("Nombre: [").append(nombre).append("] ")
				.append("Descripción: [").append(descripcion).append("] ");

		return buffer.toString();

	}

}
