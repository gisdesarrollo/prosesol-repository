package com.prosesol.springboot.app.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario", unique = true, nullable = false)
	private Long id;
	
	@Column
	@NotEmpty(message = "El nombre es obligatorio")
	private String nombre;
	
	@Column(unique = true, length = 30)
	@NotEmpty(message = "El nombre de usuario es obligatorio")
	private String username;
	
	@Column(length = 60)
	private String password;
	
	@Column
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_centro_contacto")
	@Nullable
	private CentroContacto centroContacto;
	
	private Boolean estatus;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "rel_usuarios_perfiles", joinColumns = @JoinColumn(name = "id_usuario"), 
			   inverseJoinColumns = @JoinColumn(name = "id_perfil"), uniqueConstraints = {
			   @UniqueConstraint(columnNames = { "id_usuario", "id_perfil" }) })
	private Set<Perfil> perfiles;
	
	public Usuario() {
		perfiles = new HashSet<Perfil>();
	}
	

	public Set<Perfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEstatus() {
		return estatus;
	}

	public void setEstatus(Boolean estatus) {
		this.estatus = estatus;
	}

	public CentroContacto getCentroContacto() {
		return centroContacto;
	}

	public void setCentroContacto(CentroContacto centroContacto) {
		this.centroContacto = centroContacto;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Usuario: [").append(nombre).append("]")
		       .append("Correo: [").append(email).append("]")
		       .append("Centro Contacto: [").append(centroContacto).append("]");
		
		return builder.toString();
	}

}
