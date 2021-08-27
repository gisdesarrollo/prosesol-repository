package com.prosesol.springboot.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "cat_empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cat_empresa")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "clave")
    private String clave;

    @Column(name = "consecutivo")
    private Long consecutivo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Long consecutivo) {
        this.consecutivo = consecutivo;
    }
}
