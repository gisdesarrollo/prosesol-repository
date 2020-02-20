package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

@Entity
@Table(name = "servicios")
public class Servicio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servicio", unique = true, nullable = false)
	private Long id;

	@NotEmpty(message = "Proporcione el nombre del servicio")
	@Column(name = "nombre")
	private String nombre;

	@Column(name = "notas")
	private String nota;

	@NumberFormat(style = Style.NUMBER, pattern = "#,###.##")
	@NotNull(message = "{text.servicio.inscripcionTitular}")
	@Column(name = "inscripcion_titular")
	private Double inscripcionTitular;

	@NotNull(message = "{text.servicio.costoTitular}")
	@Column(name = "costo_titular")
	private Double costoTitular;

	@NotNull(message = "{text.servicio.inscripcionBeneficiario}")
	@Column(name = "inscripcion_beneficiario")
	private Double inscripcionBeneficiario;

	@NotNull(message = "{text.servicio.costoBeneficiario}")
	@Column(name = "costo_beneficiario")
	private Double costoBeneficiario;

	@Column(name = "estatus")
	private Boolean estatus;

	@NotNull(message = "{text.servicio.tipoPrivacidad}")
	@Column(name = "tipo_privacidad")
	private Boolean tipoPrivacidad;

	@OneToMany(mappedBy = "servicio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Afiliado> afiliado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_centro_contacto")
	private CentroContacto centroContacto;

	@OneToMany(mappedBy = "servicio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RelServicioBeneficio> relServicioBeneficio;

	@OneToOne(mappedBy = "servicio", cascade = CascadeType.ALL)
	private Plan plan;

	public Servicio() {

	}

	public Servicio(String nombre, String nota, Double inscripcionTitular, Double costoTitular,
			Double inscripcionBeneficiario, Double costoBeneficiario, Boolean estatus, Boolean tipoPrivacidad,
			CentroContacto centroContacto, RelServicioBeneficio... relServicioBeneficio ) {

		this.nombre = nombre;
		this.nota = nota;
		this.inscripcionTitular = inscripcionTitular;
		this.costoTitular = costoTitular;
		this.inscripcionBeneficiario = inscripcionBeneficiario;
		this.costoBeneficiario = costoBeneficiario;
		this.estatus = estatus;
		this.tipoPrivacidad = tipoPrivacidad;
		this.centroContacto = centroContacto;
		
		for(RelServicioBeneficio beneficio : relServicioBeneficio) beneficio.setServicio(this);
		
		this.relServicioBeneficio = Stream.of(relServicioBeneficio).collect(Collectors.toSet());
		
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

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public List<Afiliado> getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(List<Afiliado> afiliado) {
		this.afiliado = afiliado;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public Double getInscripcionTitular() {
		return inscripcionTitular;
	}

	public void setInscripcionTitular(Double inscripcionTitular) {
		this.inscripcionTitular = inscripcionTitular;
	}

	public Double getCostoTitular() {
		return costoTitular;
	}

	public void setCostoTitular(Double costoTitular) {
		this.costoTitular = costoTitular;
	}

	public Double getInscripcionBeneficiario() {
		return inscripcionBeneficiario;
	}

	public void setInscripcionBeneficiario(Double inscripcionBeneficiario) {
		this.inscripcionBeneficiario = inscripcionBeneficiario;
	}

	public Double getCostoBeneficiario() {
		return costoBeneficiario;
	}

	public void setCostoBeneficiario(Double costoBeneficiario) {
		this.costoBeneficiario = costoBeneficiario;
	}

	public CentroContacto getCentroContacto() {
		return centroContacto;
	}

	public void setCentroContacto(CentroContacto centroContacto) {
		this.centroContacto = centroContacto;
	}

	public Set<RelServicioBeneficio> getRelServicioBeneficio() {
		return relServicioBeneficio;
	}

	public void setRelServicioBeneficio(Set<RelServicioBeneficio> relServicioBeneficio) {
		this.relServicioBeneficio = relServicioBeneficio;
	}

	public Boolean getTipoPrivacidad() {
		return tipoPrivacidad;
	}

	public void setTipoPrivacidad(Boolean tipoPrivacidad) {
		this.tipoPrivacidad = tipoPrivacidad;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();

		buffer.append("Nombre: [").append(nombre).append("]").append("Nota: [").append(nota).append("]")
		.append("Inscripción Titular: [").append(inscripcionTitular).append("]").append("Costo Titular: [")
		.append(costoTitular).append("]").append("Inscripción Beneficiario: [").append(inscripcionBeneficiario)
		.append("]").append("Costo Beneficiario: [").append(costoBeneficiario).append("]")
		.append("Tipo Privacidad: [").append(tipoPrivacidad).append("]")
		.append("RelServicioBeneficio: [").append(relServicioBeneficio).append("]"); 

		return buffer.toString();

	}

}
