package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Pago;

import java.util.List;

public interface IPagoService {

    public List<Pago> findAll();

    public Pago findById(Long id);
    
    public String[] getVariablesPagos();
    
    public void save(Pago pago);

    public List<String> getAllPagos();
}
