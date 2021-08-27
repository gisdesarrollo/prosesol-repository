package com.prosesol.springboot.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "parametros")
public class Parametro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametro")
    private long id;

    @Column(name = "parametro")
    private String parametro;

    @Column(name = "valor")
    private String valor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
