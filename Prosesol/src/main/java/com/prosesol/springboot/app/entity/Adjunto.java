package com.prosesol.springboot.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cat_adjuntos")
public class Adjunto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_adjunto")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "tipo_adjunto")
	private String tipoAdjunto;

	@Lob
	@Column(name = "adjunto")
	private String adjunto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_correo")
	private Correo correo;

	public Adjunto() {
	}

	public Adjunto(Long id, String nombre, String tipoAdjunto, String adjunto, Correo correo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipoAdjunto = tipoAdjunto;
		this.adjunto = adjunto;
		this.correo = correo;
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

	public String getTipoAdjunto() {
		return tipoAdjunto;
	}

	public void setTipoAdjunto(String tipoAdjunto) {
		this.tipoAdjunto = tipoAdjunto;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public Correo getCorreo() {
		return correo;
	}

	public void setCorreo(Correo correo) {
		this.correo = correo;
	}

}
