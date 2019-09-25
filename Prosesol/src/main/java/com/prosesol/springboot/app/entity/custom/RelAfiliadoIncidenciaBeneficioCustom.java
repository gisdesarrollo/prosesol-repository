package com.prosesol.springboot.app.entity.custom;

import java.util.Date;

public class RelAfiliadoIncidenciaBeneficioCustom {

    private Long idIncidencia;

    private Long idAfiliado;

    private Long idBeneficio;

    private Date fecha;

    public RelAfiliadoIncidenciaBeneficioCustom(Long idIncidencia, Long idAfiliado, Long idBeneficio, Date fecha) {
        this.idIncidencia = idIncidencia;
        this.idAfiliado = idAfiliado;
        this.idBeneficio = idBeneficio;
        this.fecha = fecha;
    }

    public Long getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Long idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public Long getIdAfiliado() {
        return idAfiliado;
    }

    public void setIdAfiliado(Long idAfiliado) {
        this.idAfiliado = idAfiliado;
    }

    public Long getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(Long idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        return sb.append("Id Incidencia: ").append(idIncidencia)
                .append("Id Afiliado: ").append(idAfiliado)
                .append("Id Beneficio: ").append(idBeneficio)
                .append("Fecha: ").append(fecha).toString();
    }
}
