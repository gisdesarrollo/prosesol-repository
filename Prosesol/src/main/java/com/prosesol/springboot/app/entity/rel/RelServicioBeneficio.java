package com.prosesol.springboot.app.entity.rel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.Servicio;
import com.prosesol.springboot.app.entity.composite.id.RelServicioBeneficioId;

@Entity
@Table(name = "rel_servicios_beneficios")
@IdClass(RelServicioBeneficioId.class)
public class RelServicioBeneficio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_servicio")
	private Servicio servicio;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_beneficio")
	private Beneficio beneficio;
	
	/*@Column(name = "titular")
	private Boolean titular;
	
	@Column(name = "beneficiario")
	private Boolean beneficiario;
	*/
	@Column(name = "descripcion")
	private String descripcion;
	
	public RelServicioBeneficio() {
		
	}
	
	public RelServicioBeneficio(Servicio servicio, Beneficio beneficio,String descripcion) {
		this.servicio = servicio;
		this.beneficio = beneficio;
		//this.titular = titular;
		//this.beneficiario = beneficiario;
		this.descripcion = descripcion;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Beneficio getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(Beneficio beneficio) {
		this.beneficio = beneficio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/*public Boolean getTitular() {
		return titular;
	}

	public void setTitular(Boolean titular) {
		this.titular = titular;
	}

	public Boolean getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Boolean beneficiario) {
		this.beneficiario = beneficiario;
	}*/

}
