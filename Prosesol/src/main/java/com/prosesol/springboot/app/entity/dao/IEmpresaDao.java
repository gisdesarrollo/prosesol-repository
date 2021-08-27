package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Empresa;
import org.springframework.data.repository.CrudRepository;

public interface IEmpresaDao extends CrudRepository<Empresa, Long> {
}
