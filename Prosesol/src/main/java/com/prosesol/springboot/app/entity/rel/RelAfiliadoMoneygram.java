package com.prosesol.springboot.app.entity.rel;

import com.prosesol.springboot.app.entity.Afiliado;

import javax.persistence.*;

@Entity
@Table(name = "rel_afiliados_moneygram")
public class RelAfiliadoMoneygram {

    @Id
    @Column(name = "id_moneygram")
    private long idMoneygram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_afiliado")
    private Afiliado afiliado;

    @Column(name = "nombre_contratante")
    private String nombreContratante;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private long telefono;

    public long getIdMoneygram() {
        return idMoneygram;
    }

    public void setIdMoneygram(long idMoneygram) {
        this.idMoneygram = idMoneygram;
    }

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public String getNombreContratante() {
        return nombreContratante;
    }

    public void setNombreContratante(String nombreContratante) {
        this.nombreContratante = nombreContratante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }
}
