package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cuentas_comerciales")
public class Cuenta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cta_comercial", unique = true, nullable = false)
	private Long id;

	@NotEmpty(message = "Escriba el nombre de la empresa")
	@Column(name = "razon_social")
	private String razonSocial;

	@NotEmpty(message = "Proporcione el RFC de la empresa")
	@Column(name = "rfc", length = 15)
	private String rfc;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "codigo_postal")
	private Integer codigoPostal;

	@Column(name = "pais")
	private String pais;

	@Column(name = "email")
	private String email;
	
	private Boolean selected;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_alta")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaAlta;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "cuentas")
	private Set<Promotor> promotores;

	@OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY)
	private Set<Afiliado> afiliado;

	@Column(name = "estatus")
	private String estatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Set<Promotor> getPromotores() {
		return promotores;
	}

	public void setPromotores(Set<Promotor> promotores) {
		this.promotores = promotores;
	}

	public Set<Afiliado> getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(Set<Afiliado> afiliado) {
		this.afiliado = afiliado;
	}
	
	public Boolean isSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}


	

}
