package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Plan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Luis Enrique Morales Soriano
 */
public interface IPlanDao extends CrudRepository<Plan, Long> {
}
