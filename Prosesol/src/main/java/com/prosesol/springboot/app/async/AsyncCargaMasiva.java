package com.prosesol.springboot.app.async;

import com.prosesol.springboot.app.exception.CustomUserException;
import com.prosesol.springboot.app.view.excel.InsertFromExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AsyncCargaMasiva {

    @Autowired
    private InsertFromExcel insertFromExcel;

    @Async("threadCargaMasiva")
    public void procesaArchivoAsync(byte[] bs) throws InterruptedException, IOException, CustomUserException {

        System.out.println("Entra al método para la lectura de archivo");
        List<String> values = new ArrayList<String>();

        Thread.sleep(5000);

        String data = new String(bs);

        InputStream inputStream = new ByteArrayInputStream(bs);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.readLine();

        String line = null;
        int counter = 1;
        while((line = br.readLine()) != null) {
            System.out.println("Read line: " + line);

            String[] valores = line.split(",");

            for (int i = 0; i < valores.length; i++) {
                System.out.println("Clave: " + i);
                System.out.println("Valor: " + valores[i]);
                values.add(valores[i]);
            }
            if (counter == 2) {
                counter = 1;

                String[] tempArray = values.toArray(new String[0]);
                insertFromExcel.insertAfiliados(tempArray);

                Thread.sleep(10000);
            }

            counter++;
        }
    }

}
