package com.prosesol.springboot.app.entity.dao;

import com.prosesol.springboot.app.entity.LogCM;

import org.springframework.data.jpa.repository.Modifying;
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
	  
	 @Modifying
	 @Query(value="delete from logs where fecha_creacion between ?1 and ?2", nativeQuery = true)
	  public void deleteAllLogs(String fecha1,String fecha2);
}
