package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Empresa;
import com.prosesol.springboot.app.entity.dao.IEmpresaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaServiceImpl implements IEmpresaService{

    @Autowired
    private IEmpresaDao empresaDao;

    @Override
    @Transactional(readOnly = true)
    public Empresa findById(Long id) {
        return empresaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Empresa empresa) {
        empresaDao.save(empresa);
    }


}
