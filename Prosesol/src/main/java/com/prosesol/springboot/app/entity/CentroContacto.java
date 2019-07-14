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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	@NotEmpty(message = "${text.centroContacto.nombre}")
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "telefono1")
	@NotNull(message = "${text.centroContacto.telefonos}")
	private Long telefono1;
	
	@Column(name = "telefono2")
	@NotNull(message = "${text.centroContacto.telefonos}")
	private Long telefono2;
	
	@Column(name = "nombre_responsable")
	private String nombreResponsable;
	
	@Column(name = "telefono_responsable")
	private Long telefonoResponsable;
	
	@Column(name = "mail_responsable")
	@Email
	private String mailResponsable;
	
	@Column(name = "estatus")
	private Boolean estatus;
	
	@OneToMany(mappedBy = "centroContacto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Servicio> servicio;
	
	@OneToOne(mappedBy = "centroContacto")
	private Usuario usuario;
	
	public CentroContacto() {
		servicio = new ArrayList<Servicio>();
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

	public String getMailResponsable() {
		return mailResponsable;
	}

	public void setMailResponsable(String mailResponsable) {
		this.mailResponsable = mailResponsable;
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
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombreResponsable() {
		return nombreResponsable;
	}

	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}

	public Long getTelefonoResponsable() {
		return telefonoResponsable;
	}

	public void setTelefonoResponsable(Long telefonoResponsable) {
		this.telefonoResponsable = telefonoResponsable;
	}

	public List<Servicio> getServicio() {
		return servicio;
	}

	public void setServicio(List<Servicio> servicio) {
		this.servicio = servicio;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
