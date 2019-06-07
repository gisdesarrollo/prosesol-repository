package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rel_afiliados_beneficiarios")
public class Beneficiario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_beneficiario")
	private Long idBeneficiario;

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_creacion")
	private Date fechaCreacion;

	@Column(name = "estatus")
	private Boolean estatus;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_afiliado")
//	private Afiliado afiliado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

//	public Afiliado getAfiliado() {
//		return afiliado;
//	}
//
//	public void setAfiliado(Afiliado afiliado) {
//		this.afiliado = afiliado;
//	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

//	@Override
//	public String toString() {
//		final StringBuilder builder = new StringBuilder();
//
//		builder.append("\n Id beneficiario:").append(idBeneficiario)
//					  .append("\n Nombre Afiliado: ").append(afiliado.getNombre())
//					  .append("\n Apellido Paterno: ").append(afiliado.getApellidoPaterno())
//					  .append("\n Apellido Materno: ").append(afiliado.getApellidoMaterno());
//		
//		return builder.toString();
//
//	}

}
