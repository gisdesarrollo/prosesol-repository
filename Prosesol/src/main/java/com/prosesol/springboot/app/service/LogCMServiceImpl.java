package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.LogCM;
import com.prosesol.springboot.app.entity.dao.ILogCMDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Service
public class LogCMServiceImpl implements ILogCMService{

    @Autowired
    private ILogCMDao iLogCMDao;

    @Override
    @Transactional(readOnly = true)
    public LogCM findById(Long id) {
        return iLogCMDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogCM> findAll() {
        return (List<LogCM>)iLogCMDao.findAll();
    }

    @Override
    @Transactional
    public void save(LogCM logCM) {
        iLogCMDao.save(logCM);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogCM> findAllLogsAfiliados() {
        return iLogCMDao.findAllLogsAfiliados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogCM> findAllLogsVigor() {
        return iLogCMDao.findAllLogsVigor();
    }
    
    @Override
	 @Transactional(readOnly = true)
	public List<LogCM> findAllLogsConciliacion() {
		 return iLogCMDao.findAllLogsConciliacion();
	}

	@Override
	@Transactional
	public void deleteAllLogs(String fechaInicio,String fechaFin) {
		iLogCMDao.deleteAllLogs(fechaInicio, fechaFin);
		
	}

	
}
