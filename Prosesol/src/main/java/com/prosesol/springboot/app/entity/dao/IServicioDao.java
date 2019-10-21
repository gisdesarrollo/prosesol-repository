package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Servicio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IServicioDao extends CrudRepository<Servicio, Long>{

    @Query("select s.nombre from Servicio s join s.afiliado as a " +
            "join a.relAfiliadoIncidencia as rai where rai.incidencia.id = ?1 " +
            "group by rai.incidencia.id")
    public String getNombreByIdIncidencia(Long id);

}
