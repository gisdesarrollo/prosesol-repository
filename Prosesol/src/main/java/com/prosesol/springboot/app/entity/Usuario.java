package com.prosesol.springboot.app.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario", unique = true, nullable = false)
	private Long id;
	
	@Column
	private String nombre;
	
	@Column(unique = true, length = 30)
	private String username;
	
	@Column(length = 60)
	private String password;
	
	@Column
	private String email;
	
	private Boolean estatus;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "usuarios", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Perfil> perfiles;
	
	public Usuario() {
		perfiles = new HashSet<Perfil>();
	}
	
//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "id_usuario")
//	private Collection<Role> roles;

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

//	public Collection<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Collection<Role> roles) {
//		this.roles = roles;
//	}
	
}
