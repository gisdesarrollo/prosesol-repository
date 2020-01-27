package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Pago;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPagoDao extends CrudRepository<Pago, Long> {

    @Query(value = "select a.rfc, p.monto, p.referencia_bancaria, p.fecha_pago, p.tipo_transaccion, " +
            "p.estatus from afiliados a, rel_afiliados_pagos rap, pagos p where a.id_afiliado = rap.id_afiliado " +
            "and rap.id_pago = p.id_pago", nativeQuery = true)
    public List<String> getAllPagos();

}
