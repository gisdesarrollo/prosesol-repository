package com.prosesol.springboot.app.async;

import com.prosesol.springboot.app.entity.LogCM;
import com.prosesol.springboot.app.service.ILogCMService;
import com.prosesol.springboot.app.view.excel.InsertCargaMasivaCSV;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AsyncCargaMasiva {

    protected final Log LOG = LogFactory.getLog(AsyncCargaMasiva.class);

    @Autowired
    private InsertCargaMasivaCSV insertCargaMasivaCSV;

    @Autowired
    private ILogCMService logCMService;

    @Async("threadCargaMasiva")
    public void procesaArchivoAsync(boolean isVigor, String nombre, byte[] bs,
                                    Long idCuentaComercial) throws InterruptedException, IOException{

        System.out.println("Entra al método para la lectura de archivo");

        Thread.sleep(5000);

        int counter = 1;
        int numeroRegistros = 1;
        String resultado = null;
        long startTime = System.nanoTime();

        List<String> log = new ArrayList<String>();
        Map<Integer, String> campos = new HashMap<Integer, String>();
        Scanner scanner = null;
        InputStream inputStream = new ByteArrayInputStream(bs);

        try{
            scanner = new Scanner(inputStream, "UTF-8");
            scanner.nextLine();
            while(scanner.hasNext()){
                String line = scanner.nextLine();

                String[] valores = line.split(",");
                int valoresCapturados = valores.length;

                for(int i = 0; i < valores.length; i++){
                    campos.put(i, valores[i]);
                }

                resultado = insertCargaMasivaCSV.evaluarDatosList(isVigor, numeroRegistros, campos, idCuentaComercial);

                if(counter == 30000) {
                    counter = 0;
                    Thread.sleep(10000);
                }

                counter++;
                numeroRegistros++;

                log.add(resultado + "\n");
            }

            generarArchivoLog(nombre, numeroRegistros, log, isVigor);

        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            if(scanner != null){
                scanner.close();
            }
        }

        long endTime = System.nanoTime();

        LOG.info("Proceso finalizado en un tiempo (segundos): " + (endTime - startTime) / 1000000000);
    }

    public void generarArchivoLog(String nombre, Integer numeroRegistros, List<String> log,
                                  boolean isVigor){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Date fechaCreacion = new Date();

        LogCM logCM = new LogCM();

        try {
            DataOutputStream dos = new DataOutputStream(bos);
            for(String str : log){
                dos.writeUTF(str);
            }

            byte[] data = bos.toByteArray();

            DateFormat getDateFormat = new SimpleDateFormat("dd/MM/yyyy'_'HH:mm:ss");
            String dateFormat = getDateFormat.format(fechaCreacion);

            logCM = new LogCM(nombre + "_" + dateFormat + ".txt", fechaCreacion,
                    numeroRegistros, data, isVigor);

            logCMService.save(logCM);
        }catch (Exception e){
            e.printStackTrace();
        }

        LOG.info("Archivo guardado correctamente");

    }

}