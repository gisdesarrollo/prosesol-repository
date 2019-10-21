package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IRelAfiliadoIncidenciaDao extends CrudRepository<RelAfiliadoIncidencia, Long>{
}
