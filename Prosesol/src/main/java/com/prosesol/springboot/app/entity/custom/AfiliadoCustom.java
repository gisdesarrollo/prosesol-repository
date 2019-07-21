package com.prosesol.springboot.app.entity.custom;

import com.prosesol.springboot.app.entity.Servicio;

public class AfiliadoCustom {

	private Long id;
	
	private String nombre;
	
	private String apellidoPaterno;
	
	private String apellidoMaterno;
	
	private String clave;
	
	private Long telefonoFijo;
	
	private Long telefonoMovil;
	
	private String municipio;
	
	private String entidadFederativa;
	
	private Servicio servicio;
	
	private int estatus;

	public AfiliadoCustom() {}

	public AfiliadoCustom(Long id, String nombre, String apellidoPaterno, String apellidoMaterno, 
			Long telefonoFijo, Long telefonoMovil, String clave, String municipio, 
			String entidadFederativa, Servicio servicio, int estatus) {
		this.id = id;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.clave = clave;
		this.telefonoFijo = telefonoFijo;
		this.telefonoMovil = telefonoMovil;
		this.municipio = municipio;
		this.entidadFederativa = entidadFederativa;
		this.servicio = servicio;
		this.estatus = estatus;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEntidadFederativa() {
		return entidadFederativa;
	}

	public void setEntidadFederativa(String entidadFederativa) {
		this.entidadFederativa = entidadFederativa;
	}
	
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	
	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Id Afiliado: [").append(id).append("]")
			   .append("Nombre: [").append(nombre).append("]")
			   .append("Apellido Paterno: [").append(apellidoPaterno).append("]")
			   .append("Apellido Materno: [").append(apellidoMaterno).append("]")
			   .append("RFC: [").append(clave).append("]")
			   .append("Telefono Fijo: [").append(telefonoFijo).append("]")
			   .append("Telefono Movil: [").append(telefonoMovil).append("]")
			   .append("Municipio: [").append(municipio).append("]")
			   .append("Entidad Federativa: [").append(entidadFederativa).append("]");
		
		return builder.toString();
	}


	
}
