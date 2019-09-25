package com.prosesol.springboot.app.entity.rel;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Beneficio;
import com.prosesol.springboot.app.entity.Incidencia;
import com.prosesol.springboot.app.entity.composite.id.RelAfiliadoIncidenciaBeneficioId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rel_afiliados_incidencias")
@IdClass(RelAfiliadoIncidenciaBeneficioId.class)
public class RelAfiliadoIncidenciaBeneficio implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_afiliado")
    private Afiliado afiliado;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_incidencia")
    private Incidencia incidencia;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_beneficio")
    private Beneficio beneficio;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Afiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Afiliado afiliado) {
        this.afiliado = afiliado;
    }

    public Beneficio getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(Beneficio beneficio) {
        this.beneficio = beneficio;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
