package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.Pago;
import org.springframework.data.repository.CrudRepository;

public interface IPagoDao extends CrudRepository<Pago, Long> {
}
