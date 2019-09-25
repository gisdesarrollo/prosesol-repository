package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidencia;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRelAfiliadoIncidenciaBeneficioDao extends CrudRepository<RelAfiliadoIncidenciaBeneficio, Long> {

}
