package com.prosesol.springboot.app.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column(name = "rfc")
    private String rfc;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
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

	@Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append("Id: ").append(id)
                .append("RFC: ") .append(rfc)
                .append("Monto: ").append(monto)
                .append("Referencia Bancaria:" ).append(referenciaBancaria)
                .append("Fecha de Pago: ").append(fechaPago)
                .append("Estatus: ").append(estatus).toString();
    }
}
