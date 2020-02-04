package com.prosesol.springboot.app.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pagos")
public class Pago implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "monto")
    private Double monto;

    @Column(name = "referencia_bancaria")
    private String referenciaBancaria;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_pago")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaPago;

    @Column(name = "estatus")
    private String estatus;

    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getReferenciaBancaria() {
        return referenciaBancaria;
    }

    public void setReferenciaBancaria(String referenciaBancaria) {
        this.referenciaBancaria = referenciaBancaria;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append("Id: ").append(id)
                .append("Monto: ").append(monto)
                .append("Referencia Bancaria:" ).append(referenciaBancaria)
                .append("Fecha de Pago: ").append(fechaPago)
                .append("Estatus: ").append(estatus)
                .append("Tipo Transacción: ").append(tipoTransaccion).toString();
    }
}
