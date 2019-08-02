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
	
	@Column(name = "proveedor")
	private String proveedor;
	
	@Column(name = "estatus")
	private int estatus;
	
	@Column(name = "fecha_creacion")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yy")
	private Date fechaCreacion;
	
	@OneToMany(mappedBy = "incidencia", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RelAfiliadoIncidencia> relAfiliadoIncidencia;
	
	public Incidencia() {
		
	}
	
	public Incidencia(String nombreAfiliado, Date fecha, String hora, String localizacion, String tipoIncidencia,
			String detalle, int estatus) {
		this.nombreAfiliado = nombreAfiliado;
		this.fecha = fecha;
		this.hora = hora;
		this.localizacion = localizacion;
		this.tipoIncidencia = tipoIncidencia;
		this.detalle = detalle;
		this.estatus = estatus;
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
	
	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	
	public Set<RelAfiliadoIncidencia> getRelAfiliadoIncidencia() {
		return relAfiliadoIncidencia;
	}

	public void setRelAfiliadoIncidencia(Set<RelAfiliadoIncidencia> relAfiliadoIncidencia) {
		this.relAfiliadoIncidencia = relAfiliadoIncidencia;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", nombreAfiliado=" + nombreAfiliado + ", fecha=" + fecha + ", hora=" + hora
				+ ", localizacion=" + localizacion + ", tipoIncidencia=" + tipoIncidencia + ", detalle=" + detalle
				+ ", proveedor=" + proveedor + ", estatus=" + estatus + ", fechaCreacion=" + fechaCreacion + "]";
	}
	
}
