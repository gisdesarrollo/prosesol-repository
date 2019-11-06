package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.entity.Pago;
import com.prosesol.springboot.app.entity.dao.IPagoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class PagoServiceImpl implements IPagoService {

    @Autowired
    private IPagoDao pagoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findAll() {
        return (List<Pago>)pagoDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pago findById(Long id) {
        return pagoDao.findById(id).orElse(null);
    }

	@Override
	public String[] getVariablesPagos() {
		Field campos[] = Pago.class.getDeclaredFields();
		String variablesAfiliado[] = new String[campos.length];
		
		for(int i = 0; i < campos.length; i++) {
			variablesAfiliado[i] = campos[i].getName();
		}
		
		
		return variablesAfiliado;
		
	}

	@Override
	@Transactional
	public void save(Pago pago) {
		pagoDao.save(pago);
	}
}
