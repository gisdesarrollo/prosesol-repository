package com.prosesol.springboot.app.util;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@Configuration
//@EnableScheduling
public class GenerarSaldoCorteScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(GenerarSaldoCorteScheduler.class);

    private Date fechaActual = new Date();

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private CalcularFecha calcularFechas;


    @Scheduled(fixedDelay = 10000)
	@Transactional
    public void calcularSaldoScheduler() {

        LOG.info("Programa de iniciación con fecha: " + fechaActual);

        String pattern = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String fecha = dateFormat.format(fechaActual);

        List<Afiliado> afiliados = afiliadoService.getAfiliadosByFechaCorte(fecha);

        for (Afiliado afiliado : afiliados) {

            LOG.info("Id Afiliado: " + afiliado.getId());

            if (afiliado.getSaldoCorte() != null) {

				Date fechaCorte = afiliado.getFechaCorte();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fechaCorte);

				Integer diaCorte = calendar.get(Calendar.DAY_OF_MONTH);

                Double saldoCorte = afiliado.getSaldoCorte();
                Double saldoAcumulado = afiliado.getSaldoAcumulado();
                Double saldoFinal;

                saldoFinal = saldoAcumulado + saldoCorte;
                Date fechaCorteActualizada = calcularFechas.calcularFechas(afiliado.getPeriodicidad(), diaCorte);

                afiliado.setSaldoCorte(saldoFinal);
                afiliado.setFechaCorte(fechaCorteActualizada);

                afiliadoService.save(afiliado);
            }
        }

    }
}
