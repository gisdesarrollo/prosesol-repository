package com.prosesol.springboot.app.entity.custom;

import java.util.Date;

public class IncidenciaCustom {

    private Long id;

    private Date fecha;

    private String detalle;

    private int estatus;

    private String nombre;

    public IncidenciaCustom(){}

    public IncidenciaCustom(Long id, Date fecha, String detalle, int estatus, String nombre) {
        this.id = id;
        this.fecha = fecha;
        this.detalle = detalle;
        this.estatus = estatus;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "IncidenciaCustom{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", detalle='" + detalle + '\'' +
                ", estatus=" + estatus +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
