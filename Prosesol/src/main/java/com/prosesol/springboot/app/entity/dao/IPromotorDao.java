package com.prosesol.springboot.app.entity.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prosesol.springboot.app.entity.Promotor;

public interface IPromotorDao extends CrudRepository<Promotor, Long>{

	@Query(value = "select p.* from promotores p  where p.clave = ?1", nativeQuery = true)
	public Promotor findByClave(String clave);
}
