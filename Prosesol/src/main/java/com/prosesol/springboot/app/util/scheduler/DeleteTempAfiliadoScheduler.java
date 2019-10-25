package com.prosesol.springboot.app.util.scheduler;

import com.prosesol.springboot.app.repository.TempAfiliadoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DeleteTempAfiliadoScheduler {

    protected static final Log LOG = LogFactory.getLog(DeleteTempAfiliadoScheduler.class);

    @Autowired
    private TempAfiliadoRepository tempAfiliadoRepository;

    @Scheduled(cron = "0 0 12 * * SAT")
    public void deleteAfiliadosTempScheduler(){

        LOG.info("Se ejecuta cron para eliminar los afiliados de la tabla temporal");
        tempAfiliadoRepository.deleleteAfiliadosTemp();
    }

}
