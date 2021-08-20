package com.prosesol.springboot.app.entity;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoMoneygram;
import com.prosesol.springboot.app.util.AfiliadoAsistencia;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "afiliados")
public class Afiliado implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_afiliado")
	private Long id;

	@Column(name = "clave")
	private String clave;

	@NotEmpty(message = "El nombre no debe quedar vacío")
	@Column(name = "nombre")
	private String nombre;

	@NotEmpty(message = "El apellido paterno no debe quedar vacío")
	@Column(name = "apellido_paterno")
	private String apellidoPaterno;

	@NotEmpty(message = "El apellido materno no debe quedar vacío")
	@Column(name = "apellido_materno")
	private String apellidoMaterno;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_nacimiento")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	private Date fechaNacimiento;

	@Column(name = "lugar_nacimiento")
	private String lugarNacimiento;

	@Column(name = "estado_civil", length = 11)
	private String estadoCivil;

	@Column(name = "dependientes")
	private Integer numeroDependientes;

	@Column(name = "ocupacion")
	private String ocupacion;

	@Column(name = "sexo", length = 10)
	private String sexo;

	@Column(name = "pais", length = 3)
	private String pais;

	@Column(name = "curp")
	private String curp;

	@Column(name = "nss")
//	@NotNull(message = "{TextField.nss.empty.afiliado.message}")
//	@NSSConstraint
	private Long nss;

	@Column(name = "rfc")
	private String rfc;

	@Column(name = "telefono_fijo")
	private Long telefonoFijo;
	
	@NotNull(message = "El teléfono móvil no debe quedar vacío")
	@Column(name = "telefono_movil")
	private Long telefonoMovil;
	
	@NotEmpty(message = "El correo electrónico no debe quedar vacío")
	@Column(name = "email")
	private String email;

	@Column(name = "direccion")
	private String direccion;

	@Column(name = "municipio")
	private String municipio;

	@Column(name = "codigo_postal")
	private String codigoPostal;

	@Column(name = "entidad_federativa", length = 3)
	private String entidadFederativa;

	@Column(name = "infonavit")
	private String infonavit;

	@Column(name = "numero_infonavit")
	private Long numeroInfonavit;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_alta")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaAlta;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_afiliacion")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaAfiliacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_corte")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaCorte;

	@Column(name = "saldo_acumulado")
	private Double saldoAcumulado;

	@Column(name = "saldo_corte")
	private Double saldoCorte;

	@NotNull
	@Column(name = "estatus", length = 1)
	private int estatus;

	@Column(name = "inscripcion")
	private Double inscripcion;

	@Column(name = "is_inscripcion")
	private Boolean isIncripcion;

	@NotNull(message = "Seleccione el tipo de servicio")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_servicio")
	private Servicio servicio;

	@NotNull(message = "Seleccione el periodo del servicio")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_periodicidad")
	private Periodicidad periodicidad;

	@Column(name = "comentarios")
	private String comentarios;

	@Column(name = "is_beneficiario")
	private Boolean isBeneficiario;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_afiliado")
	private Set<Beneficiario> beneficiarios;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_promotor")
	private Promotor promotor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cta_comercial")
	private Cuenta cuenta;
	
	@OneToMany(mappedBy = "afiliado", fetch = FetchType.LAZY)
	private Set<RelAfiliadoIncidencia> relAfiliadoIncidencia;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_afiliado")
	private Set<RelAfiliadoMoneygram> relAfiliadoMoneygram;

	@Transient
	private Integer corte;

	public Afiliado() {
		beneficiarios = new HashSet<Beneficiario>();
		relAfiliadoIncidencia = new HashSet<RelAfiliadoIncidencia>();
	}

	public Long getId() {
		return id;
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

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getEntidadFederativa() {
		return entidadFederativa;
	}

	public void setEntidadFederativa(String entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
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

	public Date getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	public Date getFechaAfiliacion() {
		return fechaAfiliacion;
	}

	public void setFechaAfiliacion(Date fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
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

	public Double getSaldoAcumulado() {
		return saldoAcumulado;
	}

	public void setSaldoAcumulado(Double saldoAcumulado) {
		this.saldoAcumulado = saldoAcumulado;
	}

	public Double getSaldoCorte() {
		return saldoCorte;
	}

	public void setSaldoCorte(Double saldoCorte) {
		this.saldoCorte = saldoCorte;
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

	public void setInscripcion(Double inscripcion) {
		this.inscripcion = inscripcion;
	}

	public Promotor getPromotor() {
		return promotor;
	}

	public void setPromotor(Promotor promotor) {
		this.promotor = promotor;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Integer getCorte() {
		return corte;
	}

	public Boolean getIsIncripcion() {
		return isIncripcion;
	}

	public void setIsIncripcion(Boolean isIncripcion) {
		this.isIncripcion = isIncripcion;
	}

	public Set<RelAfiliadoMoneygram> getRelAfiliadoMoneygram() {
		return relAfiliadoMoneygram;
	}

	public void setRelAfiliadoMoneygram(Set<RelAfiliadoMoneygram> relAfiliadoMoneygram) {
		this.relAfiliadoMoneygram = relAfiliadoMoneygram;
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder();

		builder.append("ID: ").append(id)
				.append("Nombre: ").append(nombre)
				.append("Apellido Paterno: ").append(apellidoPaterno)
				.append("Apellido Materno: ").append(apellidoMaterno);

		return builder.toString();
	}

	public String getQuery(String campo, boolean where, AfiliadoAsistencia tipoQuery, Long idCcUsuario) {
		return tipoQuery.addQuery(campo, where, idCcUsuario);
	}
}
