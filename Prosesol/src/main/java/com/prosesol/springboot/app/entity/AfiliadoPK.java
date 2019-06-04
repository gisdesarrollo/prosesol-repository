package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.Objects;

public class AfiliadoPK implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	private String rfc;
	
	public AfiliadoPK() {}
	
	public AfiliadoPK(Long id, String rfc) {
		this.id = id;
		this.rfc = rfc;
	}
	
	public Long getId() {
		return id;
	}

	public String getRfc() {
		return rfc;
	}
	
	@Override
	public boolean equals(Object obj) {

		if(obj == this) {
			return true;
		}
		if(!(obj instanceof Afiliado)) {
			return false;
		}
		
		AfiliadoPK afiliado = (AfiliadoPK)obj;
		
		return Objects.equals(id, afiliado.getId()) && Objects.equals(rfc, afiliado.getRfc());
	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, rfc);
	}
	
}
