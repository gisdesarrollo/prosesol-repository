package com.prosesol.springboot.app.entity.composite.id;

import java.io.Serializable;

public class RelServicioBeneficioId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long servicio;
	
	private Long beneficio;
	
	public RelServicioBeneficioId() {}

	public RelServicioBeneficioId(Long servicio, Long beneficio) {
		super();
		this.servicio = servicio;
		this.beneficio = beneficio;
	}
	
	public Long getServicio() {
		return servicio;
	}

	public void setServicio(Long servicio) {
		this.servicio = servicio;
	}

	public Long getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(Long beneficio) {
		this.beneficio = beneficio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beneficio == null) ? 0 : beneficio.hashCode());
		result = prime * result + ((servicio == null) ? 0 : servicio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelServicioBeneficioId other = (RelServicioBeneficioId) obj;
		if (beneficio == null) {
			if (other.beneficio != null)
				return false;
		} else if (!beneficio.equals(other.beneficio))
			return false;
		if (servicio == null) {
			if (other.servicio != null)
				return false;
		} else if (!servicio.equals(other.servicio))
			return false;
		return true;
	}	
}
