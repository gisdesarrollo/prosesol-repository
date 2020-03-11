package com.prosesol.springboot.app.util.scheduler;

import com.prosesol.springboot.app.entity.Afiliado;
import com.prosesol.springboot.app.service.IAfiliadoService;
import com.prosesol.springboot.app.util.CalcularFecha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@EnableScheduling
public class GenerarSaldoCorteScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(GenerarSaldoCorteScheduler.class);

    private final Date fechaActual = new Date();

    @Autowired
    private IAfiliadoService afiliadoService;

    @Autowired
    private CalcularFecha calcularFechas;

    @Scheduled(cron = "0 0 2 * * ?", zone = "America/Mexico_City")
    @Transactional
    public void calcularSaldoScheduler() {
        LOG.info("Programa de iniciación con fecha: " + fechaActual);

        Calendar getCurrentTime = new GregorianCalendar(TimeZone.getTimeZone("America/Mexico_City"));
        String pattern = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String fecha = dateFormat.format(fechaActual);

        List<Afiliado> afiliados = afiliadoService.getAfiliadosByFechaCorte(fecha);

        int hour = getCurrentTime.get(Calendar.HOUR);
        int minute = getCurrentTime.get(Calendar.MINUTE);
        int second = getCurrentTime.get(Calendar.SECOND);
        int year = getCurrentTime.get(Calendar.YEAR);

        LOG.info("Hora Actual: " + hour + ":" + minute + ":" + second + " Año en curso: " + year);

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
