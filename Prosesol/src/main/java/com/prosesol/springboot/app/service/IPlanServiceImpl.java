package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Plan;
import com.prosesol.springboot.app.entity.dao.IPlanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Luis Enrique Morales Soriano
 */
@Service
public class IPlanServiceImpl implements IPlanService {

    @Autowired
    private IPlanDao planDao;

    @Override
    @Transactional(readOnly = true)
    public Plan findById(Long id) {
        return planDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Plan plan) {
        planDao.save(plan);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        planDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> findAll() {
        return (List<Plan>)planDao.findAll();
    }
}
