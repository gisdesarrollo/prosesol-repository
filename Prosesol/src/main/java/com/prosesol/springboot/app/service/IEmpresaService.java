package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Empresa;

public interface IEmpresaService {

    public Empresa findById(Long id);

    public void save(Empresa empresa);
}
