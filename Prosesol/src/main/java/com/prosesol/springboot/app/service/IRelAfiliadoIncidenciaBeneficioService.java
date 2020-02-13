package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.rel.RelAfiliadoIncidenciaBeneficio;

import java.util.Date;
import java.util.List;

public interface IRelAfiliadoIncidenciaBeneficioService {

    public List<RelAfiliadoIncidenciaBeneficio> findAll();

    public RelAfiliadoIncidenciaBeneficio findById(Long id);

    public void save(RelAfiliadoIncidenciaBeneficio relAfiliadoIncidenciaBeneficio);

    public void deleteById(Long id);

    public List getRelAfiliadoIncidenciaBeneficioByIdIncidencia(Long id);
    
    public void deleteRelAfiliadoIncidenciaById(Long id);
    
    public void deleteRelAfiliadoIncidenciaByIdIncidenciaAndIdBeneneficio(Long idIncidencia,Long idBeneficio);
    
    public void updateRelAfiliadoIncidencia(String beneficio,Date fecha,Long idIncidencia);
}
