package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Empresa;

import java.util.List;

public interface IEmpresaService {

    public Empresa findById(Long id);

    public void save(Empresa empresa);

    public void delete(Empresa empresa);

    public List<Empresa> findAll();
}
