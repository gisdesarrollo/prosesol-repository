package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.LogCM;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ILogCMDao extends CrudRepository<LogCM, Long> {

	 @Query("select l from LogCM l where l.isVigor is false and l.isConciliacion is false")
	    public List<LogCM> findAllLogsAfiliados();

	  @Query("select l from LogCM l where l.isVigor is true")
	    public List<LogCM> findAllLogsVigor();
	    
	  @Query("select l from LogCM l where l.isVigor is false and l.isConciliacion is true")
	    public List<LogCM> findAllLogsConciliacion();

}
