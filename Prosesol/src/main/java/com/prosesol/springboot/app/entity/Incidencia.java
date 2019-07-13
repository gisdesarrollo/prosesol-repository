package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;

@Entity
@Table(name = "incidencias")
public class Incidencia implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_incidencia")
	private Long id;
	
	@Column(name = "nombre_afiliado")
	private String nombreAfiliado;
	
	@Column(name = "fecha")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yy")
	private Date fecha;
	
	@Column(name = "hora")
	private String hora;
	
	@Column(name = "localizacion")
	private String localizacion;
	
	@Column(name = "tipo_incidencia")
	private String tipoIncidencia;
	
	@Column(name = "detalle")
	private String detalle;
	
	@Column(name = "estatus")
	private String estatus;
	
	@OneToMany(mappedBy = "incidencia", cascade = CascadeType.ALL)
	private List<RelAfiliadoIncidencia> relAfiliadoIncidencia;
	
	public Incidencia() {
		relAfiliadoIncidencia = new ArrayList<RelAfiliadoIncidencia>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreAfiliado() {
		return nombreAfiliado;
	}

	public void setNombreAfiliado(String nombreAfiliado) {
		this.nombreAfiliado = nombreAfiliado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getTipoIncidencia() {
		return tipoIncidencia;
	}

	public void setTipoIncidencia(String tipoIncidencia) {
		this.tipoIncidencia = tipoIncidencia;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	public List<RelAfiliadoIncidencia> getRelAfiliadoIncidencia() {
		return relAfiliadoIncidencia;
	}

	public void setRelAfiliadoIncidencia(List<RelAfiliadoIncidencia> relAfiliadoIncidencia) {
		this.relAfiliadoIncidencia = relAfiliadoIncidencia;
	}
}
