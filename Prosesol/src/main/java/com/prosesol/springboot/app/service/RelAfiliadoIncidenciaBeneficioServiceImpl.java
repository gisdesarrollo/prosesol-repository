package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.dao.IRelAfiliadoIncidenciaBeneficioDao;
import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;
import com.prosesol.springboot.app.repository.RelAfiliadoIncidenciaBeneficioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RelAfiliadoIncidenciaBeneficioServiceImpl implements IRelAfiliadoIncidenciaBeneficioService{

    @Autowired
    private IRelAfiliadoIncidenciaBeneficioDao relAfiliadoIncidenciaBeneficioDao;

    @Autowired
    private RelAfiliadoIncidenciaBeneficioRepository relAfiliadoIncidenciaBeneficioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RelAfiliadoIncidenciaBeneficio> findAll() {
        return (List<RelAfiliadoIncidenciaBeneficio>)relAfiliadoIncidenciaBeneficioDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RelAfiliadoIncidenciaBeneficio findById(Long id) {
        return relAfiliadoIncidenciaBeneficioDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(RelAfiliadoIncidenciaBeneficio relAfiliadoIncidenciaBeneficio) {
        relAfiliadoIncidenciaBeneficioDao.save(relAfiliadoIncidenciaBeneficio);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        relAfiliadoIncidenciaBeneficioDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List getRelAfiliadoIncidenciaBeneficioByIdIncidencia(Long id) {
        return relAfiliadoIncidenciaBeneficioRepository.getRelAfiliadoIncidenciaBeneficioByIdIncidencia(id);
    }

	@Override
	@Transactional
	public void deleteRelAfiliadoIncidenciaById(Long id) {
		relAfiliadoIncidenciaBeneficioDao.deleteRelAfiliadoIncidenciaById(id);
		
	}

	@Override
	@Transactional
	public void deleteRelAfiliadoIncidenciaByIdIncidenciaAndIdBeneneficio(Long idIncidencia, Long idBeneficio) {
		relAfiliadoIncidenciaBeneficioDao.deleteRelAfiliadoIncidenciaByIdIncidenciaAndIdBeneneficio(idIncidencia, idBeneficio);
		
	}

	@Override
	@Transactional
	public void updateRelAfiliadoIncidencia(String beneficio,Date fecha,Long idIncidencia) {
		relAfiliadoIncidenciaBeneficioDao.updateRelAfiliadoIncidencia(beneficio,fecha,idIncidencia);
		
	}

}
