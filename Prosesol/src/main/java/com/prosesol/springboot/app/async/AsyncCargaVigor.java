package com.prosesol.springboot.app.async;

import com.prosesol.springboot.app.entity.LogCM;
import com.prosesol.springboot.app.exception.CustomExcelException;
import com.prosesol.springboot.app.repository.TempAfiliadoRepository;
import com.prosesol.springboot.app.service.ILogCMService;
import com.prosesol.springboot.app.view.excel.InsertCargaMasivaCSV;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AsyncCargaVigor {

    protected final Log LOG = LogFactory.getLog(AsyncCargaMasiva.class);

    @Autowired
    private InsertCargaMasivaCSV insertCargaMasivaCSV;

    @Autowired
    private ILogCMService logCMService;

    @Autowired
    private TempAfiliadoRepository tempAfiliadoRepository;

    @Async("threadCargaVigor")
    public void procesaArchivoVigorAsync(boolean isVigor,boolean isConciliacion, String nombre, byte[] bs
            , Long idCuentaComercial)
            throws InterruptedException, IOException {

        LOG.info("Entra al método para la lectura de archivo");

        Thread.sleep(5000);
        tempAfiliadoRepository.insertAfiliadosOnTemp(idCuentaComercial);

        int counter = 1;
        int numeroRegistros = 1;
        String resultado = null;
        long startTime = System.nanoTime();

        List<String> log = new ArrayList<String>();
        Map<Integer, String> campos = new HashMap<Integer, String>();
        Scanner scanner = null;
        InputStream inputStream = new ByteArrayInputStream(bs);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        try{
        	String cadena ="";
       	 	in.readLine();
       	 	String[] valores = null;
       	 	while ((cadena = in.readLine()) != null) {
       		 
       	 		valores = cadena.split(",");
       	 		for(Integer i = 0; i< valores.length; i++) {
       	 			campos.put(i, valores[i]);
       	 		}

                resultado = insertCargaMasivaCSV.evaluarDatosList(isVigor,isConciliacion,
                        numeroRegistros, campos, idCuentaComercial);

                if(counter == 30000) {
                    counter = 0;
                    Thread.sleep(10000);
                }

                counter++;
                numeroRegistros++;

                log.add(resultado + "\n");
            }
       	 	numeroRegistros--;
            generarArchivoLog(nombre, numeroRegistros, log, isVigor,isConciliacion);

        }catch(CustomExcelException ce){
            ce.printStackTrace();
            List<String> errorLog = new ArrayList<String>();

            for(String res : log){
                errorLog.add(res);
            }

            errorLog.add(StringEscapeUtils.unescapeHtml4(ce.getMessage()));
            tempAfiliadoRepository.updateAfiliadosByAfiliadosTemp();
            tempAfiliadoRepository.deleteAfiliadosOnTempByFechaActual();

            generarArchivoLog(nombre, numeroRegistros, errorLog, isVigor,isConciliacion);

        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            if(scanner != null){
                scanner.close();
            }
        }

        long endTime = System.nanoTime();

        LOG.info("Proceso finalizado en un tiempo (segundos): " +
                (endTime - startTime) / 1000000000);
    }

    public void generarArchivoLog(String nombre, Integer numeroRegistros, List<String> log,
                                    boolean isVigor,boolean isConciliacion){

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Date fechaCreacion = new Date();

        LogCM logCM = new LogCM();
        
        try {
        	
            for(String str : log){
            	byteArray.write(str.getBytes());
               
            }

            byte[] data = byteArray.toByteArray();

            DateFormat getDateFormat = new SimpleDateFormat("dd/MM/yyyy'_'HH:mm:ss");
            String dateFormat = getDateFormat.format(fechaCreacion);

            logCM = new LogCM(nombre + "_" + dateFormat + ".txt", fechaCreacion,
                    numeroRegistros, data, isVigor,isConciliacion,false);

            logCMService.save(logCM);
            byteArray.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        LOG.info("Archivo guardado correctamente");

    }

}
