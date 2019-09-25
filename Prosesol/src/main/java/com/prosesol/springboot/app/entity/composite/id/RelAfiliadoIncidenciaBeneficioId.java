package com.prosesol.springboot.app.entity.composite.id;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;

import java.io.Serializable;

public class RelAfiliadoIncidenciaBeneficioId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long afiliado;

    private Long beneficio;

    private Long incidencia;

    public RelAfiliadoIncidenciaBeneficioId() {

    }

    public RelAfiliadoIncidenciaBeneficioId(Long incidencia, Long afiliado, Long beneficio) {

        this.incidencia = incidencia;
        this.afiliado = afiliado;
        this.beneficio = beneficio;
    }

    public Long getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Long afiliado) {
        this.afiliado = afiliado;
    }

    public Long getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(Long beneficio) {
        this.beneficio = beneficio;
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
        result = prime * result + ((beneficio == null) ? 0 : beneficio.hashCode());
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
        RelAfiliadoIncidenciaBeneficioId other = (RelAfiliadoIncidenciaBeneficioId) obj;
        if (afiliado == null) {
            if (other.afiliado != null)
                return false;
        } else if (!afiliado.equals(other.afiliado))
            return false;
        if (beneficio == null) {
            if (other.beneficio != null)
                return false;
        } else if (!beneficio.equals(other.beneficio))
            return false;
        if (incidencia == null) {
            if (other.incidencia != null)
                return false;
        } else if (!incidencia.equals(other.incidencia))
            return false;
        return true;
    }
}
