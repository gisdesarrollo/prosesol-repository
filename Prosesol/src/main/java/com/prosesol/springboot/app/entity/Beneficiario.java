package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "beneficiarios")
public class Beneficiario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_beneficiario", unique = true, nullable = false)
	private Long id;
	
	@NotEmpty(message = "El nombre no puede quedar vacío")
	@Column(name = "nombre")
	private String nombre;
	
	@NotEmpty(message = "El apellido paterno no puede quedar vacío")
	@Column(name = "apellido_paterno")
	private String apellidoPaterno;
	
	@NotEmpty(message = "El apellido materno no puede quedart vacío")
	@Column(name = "apellido_materno")
	private String apellidoMaterno;
		
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date fechaNacimiento;
	
	@Column(name="telefono_fijo")
	private Long telefonoFijo;
	
	@Column(name="telefono_movil")
	private Long telefonoMovil;
	
	@NotEmpty(message = "El email no debe quedar vacío")
	@Column(name="email")
	private String email;
	
	@NotEmpty(message = "Por favor, proporcione la dirección")
	@Column(name="direccion")
	private String direccion;
	
	@Column(name="municipio")
	private String municipio;
	
	@Column(name="codigo_postal")
	private Long codigoPostal;
	
	@Column(name="entidad_federativa")
	private String entidadFederativa;
	
	@Column(name = "nss")
	private Long nss; 
	
	@Column(name = "rfc")
	private String rfc;
	
	@Column(name = "curp")
	private String curp;

	@ManyToMany(mappedBy = "beneficiarios", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Afiliado> afiliados = new HashSet<Afiliado>();
	
	public Beneficiario() {
		
	}

	public Beneficiario(@NotEmpty(message = "El nombre no puede quedar vacío") String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public Beneficiario(@NotEmpty(message = "El nombre no puede quedar vacío") String nombre,
			Set<Afiliado> afiliados) {
		super();
		this.nombre = nombre;
		this.afiliados = afiliados;
	}



	public Set<Afiliado> getAfiliados() {
		return afiliados;
	}

	public void setAfiliados(Set<Afiliado> afiliados) {
		this.afiliados = afiliados;
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getNss() {
		return nss;
	}

	public void setNss(Long nss) {
		this.nss = nss;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
	}

	public Long getTelefonoFijo() {
		return telefonoFijo;
	}

	public void setTelefonoFijo(Long telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public Long getTelefonoMovil() {
		return telefonoMovil;
	}

	public void setTelefonoMovil(Long telefonoMovil) {
		this.telefonoMovil = telefonoMovil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public Long getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(Long codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getEntidadFederativa() {
		return entidadFederativa;
	}

	public void setEntidadFederativa(String entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
	}
	
	public void addAfiliado(Afiliado afiliado) {
		
		afiliados.add(afiliado);
		afiliado.getBeneficiarios().add(this);
		
	}
	
	@Override
	public String toString() {
		
		final StringBuilder builder = new StringBuilder();
		
		builder.append("Id Beneficiario: ").append("")
			   .append(id).append("")
			   .append(" Nombre Beneficiario: ").append("")
			   .append(nombre);
		
		return builder.toString();
	}
	
}
