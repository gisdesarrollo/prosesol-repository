package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Pago;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPagoDao extends CrudRepository<Pago, Long> {

    @Query(value = "select a.rfc, p.monto, p.referencia_bancaria, p.fecha_pago, p.tipo_transaccion,"
    		+ "p.estatus,p.id_transaccion,a.nombre,a.apellido_paterno,a.apellido_materno,coalesce(se.nombre,'default_value') as sNombre,cm.razon_social  from afiliados a, rel_afiliados_pagos rap, pagos p,servicios se,cuentas_comerciales cm "
    		+ " where a.id_afiliado = rap.id_afiliado " +
            "and rap.id_pago = p.id_pago and a.id_servicio = se.id_servicio "
            + "and a.id_cta_comercial = cm.id_cta_comercial and p.estatus = 'completed' ", nativeQuery = true)
    public List<String> getAllPagos();
    
    @Query(value = "select a.rfc, p.monto, p.referencia_bancaria, p.fecha_pago, p.tipo_transaccion,"
    		+ "p.estatus,p.id_transaccion,a.nombre,a.apellido_paterno,a.apellido_materno,coalesce(se.nombre,'default_value') as sNombre,cm.razon_social  from afiliados a, rel_afiliados_pagos rap, pagos p,servicios se,cuentas_comerciales cm "
    		+ " where a.id_afiliado = rap.id_afiliado " +
            "and rap.id_pago = p.id_pago and a.id_servicio = se.id_servicio "
            + "and a.id_cta_comercial = cm.id_cta_comercial and p.estatus = 'completed' and p.fecha_pago between ?1 and ?2", nativeQuery = true)
    public List<String> getPagosByFechaInicialAndFechaFinal(String fechaInicial,String fechaFinal);
    
    @Query(value = "select a.rfc, p.monto, p.referencia_bancaria, p.fecha_pago, p.tipo_transaccion,"
    		+ "p.estatus,p.id_transaccion,a.nombre,a.apellido_paterno,a.apellido_materno,coalesce(se.nombre,'default_value') as sNombre,cm.razon_social  from afiliados a, rel_afiliados_pagos rap, pagos p,servicios se,cuentas_comerciales cm "
    		+ " where a.id_afiliado = rap.id_afiliado " +
            "and rap.id_pago = p.id_pago and a.id_servicio = se.id_servicio "
            + "and a.id_cta_comercial = cm.id_cta_comercial and p.estatus = 'completed' and p.fecha_pago between ?1 and ?2 and p.tipo_transaccion = ?3", nativeQuery = true)
    public List<String> getPagosByFechasAndFormaPago(String fechaInicial,String fechaFinal,String formaPago);

}
