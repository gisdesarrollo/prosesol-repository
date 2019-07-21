package com.prosesol.springboot.app.entity.dto;

import java.util.List;

import com.prosesol.springboot.app.entity.rel.RelServicioBeneficio;

public class RelServicioBeneficioDto {

	private List<RelServicioBeneficio> relServicioBeneficios;
	
	public List<RelServicioBeneficio> getRelServicioBeneficios() {
		return relServicioBeneficios;
	}

	public void setRelServicioBeneficios(List<RelServicioBeneficio> relServicioBeneficios) {
		this.relServicioBeneficios = relServicioBeneficios;
	}
}
