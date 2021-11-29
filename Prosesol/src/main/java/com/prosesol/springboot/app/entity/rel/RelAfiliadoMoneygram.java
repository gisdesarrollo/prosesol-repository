package com.prosesol.springboot.app.entity.rel;

import com.prosesol.springboot.app.entity.Afiliado;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rel_afiliados_moneygram")
public class RelAfiliadoMoneygram {

    @Id
    @Column(name = "id_moneygram")
    private String idMoneygram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_afiliado")
    private Afiliado afiliado;

    @Column(name = "nombre_contratante")
    @NotEmpty(message = "Campo obligatorio")
    private String nombreContratante;

    @Column(name = "email")
    @NotEmpty(message = "Campo obligatorio")
    private String emailContratante;

    @Column(name = "telefono")
    @NotNull(message = "Campo obligatorio")
    private long telefonoContratante;

    public String getIdMoneygram() {
        return idMoneygram;
    }

    public void setIdMoneygram(String idMoneygram) {
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

    public String getEmailContratante() {
        return emailContratante;
    }

    public void setEmailContratante(String emailContratante) {
        this.emailContratante = emailContratante;
    }

    public long getTelefonoContratante() {
        return telefonoContratante;
    }

    public void setTelefonoContratante(long telefonoContratante) {
        this.telefonoContratante = telefonoContratante;
    }
}
