package com.prosesol.springboot.app.entity.composite.id;

import java.io.Serializable;

public class
RelAfiliadoIncidenciaId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long afiliado;
	
	private Long incidencia;
	
	public RelAfiliadoIncidenciaId() {
		
	}

	public RelAfiliadoIncidenciaId(Long incidencia, Long afiliado) {
		
		this.incidencia = incidencia;
		this.afiliado = afiliado;
	}
	
	public Long getAfiliado() {
		return afiliado;
	}

	public void setAfiliado(Long afiliado) {
		this.afiliado = afiliado;
	}
	
	public Long getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Long incidencia) {
		this.incidencia = incidencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((afiliado == null) ? 0 : afiliado.hashCode());
		result = prime * result + ((incidencia == null) ? 0 : incidencia.hashCode());
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
		RelAfiliadoIncidenciaId other = (RelAfiliadoIncidenciaId) obj;
		if (afiliado == null) {
			if (other.afiliado != null)
				return false;
		} else if (!afiliado.equals(other.afiliado))
			return false;
		if (incidencia == null) {
			if (other.incidencia != null)
				return false;
		} else if (!incidencia.equals(other.incidencia))
			return false;
		return true;
	}	
}
