package com.prosesol.springboot.app.service;

import com.prosesol.springboot.app.entity.LogCM;

import java.util.List;

public interface ILogCMService {

    public LogCM findById(Long id);

    public List<LogCM> findAll();

    public void save(LogCM logCM);

    public List<LogCM> findAllLogsAfiliados();

    public List<LogCM> findAllLogsVigor();
    
    public List<LogCM> findAllLogsConciliacion();
    
    public List<LogCM> findAllLogsMoneygram();
    
    public void deleteAllLogs(String fechaInicio,String fechaFin);

}
