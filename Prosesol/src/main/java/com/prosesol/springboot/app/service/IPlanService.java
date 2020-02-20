package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Plan;

import java.util.List;

/**
 * @author Luis Enrique Morales Soriano
 */
public interface IPlanService {

    public Plan findById(Long id);

    public void save(Plan plan);

    public void deleteById(Long id);

    public List<Plan> findAll();

}
