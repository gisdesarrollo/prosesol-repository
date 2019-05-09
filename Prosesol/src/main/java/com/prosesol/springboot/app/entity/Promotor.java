package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "promotores")
public class Promotor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_promotor", unique = true, nullable = false)
	private Long id;

	@NotEmpty(message = "Proporcione el nombre del promotor")
	@Column(name = "nombre")
	private String nombre;

	@NotEmpty(message = "Proporcione el Apellido Paterno")
	@Column(name = "apellido_paterno")
	private String apellidoPaterno;

	@NotEmpty(message = "Proporcione el Apellido Materno")
	@Column(name = "apellido_materno")
	private String apellidoMaterno;

	@NotNull(message = "Coloque una fecha")
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_alta")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date fechaAlta;

	@Column(name = "edad")
	private Integer edad;

	@NotEmpty(message = "Proporcione el email del promotor")
	@Column(name = "email")
	private String email;

	@Column(name = "estatus")
	private Boolean estatus;

	@OneToMany(mappedBy = "promotor", fetch = FetchType.LAZY)
	private Set<Afiliado> afiliado;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "rel_promotores_cuentas", joinColumns = @JoinColumn(name = "id_promotor"), inverseJoinColumns = @JoinColumn(name = "id_cta_comercial"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"id_promotor", "id_cta_comercial" }))
	private Set<Cuenta> cuentas;

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

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Set<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(Set<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public Set<Afiliado> getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(Set<Afiliado> afiliado) {
		this.afiliado = afiliado;
	}

}
