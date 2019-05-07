package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cat_correos")
public class Correo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_correo")
	private Long id;
	
	@Column(name = "tipo_correo")
	private String tipoCorreo;
	
	@Lob
	@Column(name = "html")
	private String html;
	
	@OneToMany(mappedBy = "correo", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
			  orphanRemoval = true)
	private List<Adjunto> adjuntos;
	
	public Correo() {}

	public Correo(Long id, String tipoCorreo, String html) {
		super();
		this.id = id;
		this.tipoCorreo = tipoCorreo;
		this.html = html;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoCorreo() {
		return tipoCorreo;
	}

	public void setTipoCorreo(String tipoCorreo) {
		this.tipoCorreo = tipoCorreo;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	
}
