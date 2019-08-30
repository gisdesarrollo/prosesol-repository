package com.prosesol.springboot.app.async;

import com.prosesol.springboot.app.exception.CustomUserException;
import com.prosesol.springboot.app.util.AfiliadoExcelEval;
import com.prosesol.springboot.app.view.excel.InsertFromExcel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class AsyncCargaMasiva {

    protected final Log LOG = LogFactory.getLog(AsyncCargaMasiva.class);

    @Autowired
    private InsertFromExcel insertFromExcel;

    @Async("threadCargaMasiva")
    public void procesaArchivoAsync(byte[] bs) throws InterruptedException, IOException, CustomUserException {

        System.out.println("Entra al método para la lectura de archivo");
        List<String> values = new ArrayList<String>();

        int startIndex = 0;
        int endIndex = 0;

        long startTime = System.nanoTime();

        Thread.sleep(5000);

        String data = new String(bs);

        InputStream inputStream = new ByteArrayInputStream(bs);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.readLine();

        String line = null;
        int counter = 1;
        while((line = br.readLine()) != null) {
            String[] valores = line.split(",");

            int valoresCapturados = valores.length;

            insertFromExcel.insertAfiliados(valores, valoresCapturados);

            if(counter == 30000){
                counter = 0;
                Thread.sleep(10000);
            }

//            for (int i = 0; i < valores.length; i++) {
//                values.add(valores[i]);
//                endIndex = values.size();
//            }
//            if (counter == 30000) {
//                counter = 0;
//
//                String[] copy = values.toArray(new String[0]);
//
//                String[] tempArray = Arrays.copyOfRange(copy, startIndex, endIndex);
//                insertFromExcel.insertAfiliados(tempArray, valoresCapturados);
//
//                startIndex = endIndex;
//
//                Thread.sleep(10000);
//            }

            System.gc();
            counter++;
        }

        long endTime = System.nanoTime();

        LOG.info("Proceso finalizado en un tiempo (segundos): " + (endTime - startTime) / 1000000000);
    }

}
