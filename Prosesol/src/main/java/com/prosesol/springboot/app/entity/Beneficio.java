package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Table(name = "beneficios")
@Entity
public class Beneficio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_beneficio", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
		
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "beneficios")
	private List<Servicio> servicios;
	
	public Beneficio() {
		servicios = new ArrayList<Servicio>();
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

	public List<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}
	
	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Id: [").append(id).append("] ")
			  .append("Nombre: [").append(nombre).append("] ")
			  .append("Descripción: [").append(descripcion).append("] ");
		
		return buffer.toString();
		
	}


	
}
