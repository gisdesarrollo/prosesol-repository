package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="afiliados")
public class Afiliado implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_afiliado", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "clave")
	@NotEmpty(message = "Ingrese una clave")
	private String clave;
	
	@NotEmpty(message = "El nombre no debe quedar vacío")
	@Column(name="nombre")
	private String nombre;
	
	@NotEmpty(message = "El apellido paterno no debe quedar vacío")
	@Column(name="apellido_paterno")
	private String apellidoPaterno;
	
	@NotEmpty(message = "El apellido materno no debe quedar vacío")
	@Column(name="apellido_materno")
	private String apellidoMaterno;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date fechaNacimiento;
	
	@Column(name="lugar_nacimiento")
	private String lugarNacimiento;
	
	@Column(name="estado_civil", length = 10)
	private String estadoCivil;
	
	@Column(name="regimen_matrimonial")
	private String regimenMatrimonial;
	
	@Column(name="dependientes")
	private Integer numeroDependientes;	
	
	@Column(name="ocupacion")
	private String ocupacion;
	
	@Column(name="escolaridad")
	private String escolaridad;
	
	@Column(name="sexo", length = 10)
	private String sexo;
	
	@Column(name="pais", length = 3)
	private String pais;
	
	@Column(name="curp", length = 18)
	private String curp;
	
	@Column(name="nss")
	private Long nss;
	
	@Column(name="rfc", length = 13)
	private String rfc;
	
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
	
	@Column(name="entidad_federativa", length = 3)
	private String entidadFederativa;
	
	@Column(name="estatus_vivienda", length = 10)
	private String estatusVivienda;
	
	@Column(name="infonavit")
	private String infonavit;
	
	@Column(name="numero_infonavit")
	private Long numeroInfonavit;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_alta")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date fechaAlta;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_pago")
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date fechaPago;
	
	@Column(name="estatus", length = 1)
	private Boolean estatus;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Servicio servicio;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Periodicidad periodicidad;
		
	@Column(name="comentarios")
	private String comentarios;
	
	@Column(name = "is_beneficiario")
	private Boolean isBeneficiario;
	
	@OneToMany(mappedBy = "afiliado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Beneficiario> beneficiarios;

	public Afiliado() {
		beneficiarios = new HashSet<Beneficiario>();
	}
	
	public Set<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(Set<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
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

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getRegimenMatrimonial() {
		return regimenMatrimonial;
	}

	public void setRegimenMatrimonial(String regimenMatrimonial) {
		this.regimenMatrimonial = regimenMatrimonial;
	}

	public Integer getNumeroDependientes() {
		return numeroDependientes;
	}

	public void setNumeroDependientes(Integer numeroDependientes) {
		this.numeroDependientes = numeroDependientes;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public String getEscolaridad() {
		return escolaridad;
	}

	public void setEscolaridad(String escolaridad) {
		this.escolaridad = escolaridad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCurp() {
		return curp;
	}

	public void setCurp(String curp) {
		this.curp = curp;
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

	public String getEstatusVivienda() {
		return estatusVivienda;
	}

	public void setEstatusVivienda(String estatusVivienda) {
		this.estatusVivienda = estatusVivienda;
	}

	public String getInfonavit() {
		return infonavit;
	}

	public void setInfonavit(String infonavit) {
		this.infonavit = infonavit;
	}

	public Long getNumeroInfonavit() {
		return numeroInfonavit;
	}

	public void setNumeroInfonavit(Long numeroInfonavit) {
		this.numeroInfonavit = numeroInfonavit;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public void addBeneficiario(Beneficiario beneficiario) {
		beneficiarios.add(beneficiario);	
	}

	public Boolean getIsBeneficiario() {
		return isBeneficiario;
	}

	public void setIsBeneficiario(Boolean isBeneficiario) {
		this.isBeneficiario = isBeneficiario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Periodicidad getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(Periodicidad periodicidad) {
		this.periodicidad = periodicidad;
	}

	@Override
	public String toString() {
		
		final StringBuilder builder = new StringBuilder();
		
		for(Beneficiario beneficiario : beneficiarios) {
			
			builder.append("\n Id Afiliado: ").append("")
			   .append(id).append("")
			   .append("\n Nombre Afiliado: ").append("")
			   .append(nombre)
			   .append(beneficiario)
			   .append("\n Tipo servicio: ")
			   .append(servicio.getId())
			   .append("\n Tipo Periodo: ")
			   .append(periodicidad.getId());
			
		}
		
		
		
		return builder.toString();
	}
	
}
