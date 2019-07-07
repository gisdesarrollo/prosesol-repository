package com.prosesol.springboot.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Entity
@Table(name = "centros_contactos")
public class CentroContacto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_centro_contacto")
	private Long id;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "correo")
	@Email
	private String correo;
	
	@Column(name = "telefono1")
	private Long telefono1;
	
	@Column(name = "telefono2")
	private Long telefono2;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@OneToOne(mappedBy = "centroContacto")
	private Servicio servicio;
	
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Long getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(Long telefono1) {
		this.telefono1 = telefono1;
	}

	public Long getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(Long telefono2) {
		this.telefono2 = telefono2;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}
	
}
