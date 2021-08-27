package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Parametro;
import com.prosesol.springboot.app.entity.dao.IParametroDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParametroServiceImpl implements IParametroService{

    @Autowired
    private IParametroDao parametroDao;

    @Override
    @Transactional
    public Parametro findById(Long id) {
        return parametroDao.findById(id).orElse(null);
    }
}
