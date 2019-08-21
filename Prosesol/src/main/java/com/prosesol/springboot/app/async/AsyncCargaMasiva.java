package com.prosesol.springboot.app.async;

import org.apache.commons.io.input.CharSequenceReader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class AsyncCargaMasiva {

    @Async("threadCargaMasiva")
    public void procesaArchivoAsync(byte[] bs) throws InterruptedException, IOException {

        System.out.println("Entra al método para la lectura de archvio");

        Thread.sleep(5000);

        String data = new String(bs);

        InputStream inputStream = new ByteArrayInputStream(bs);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while((line = br.readLine()) != null){
            System.out.println("Read line: " + line);
        }

    }

}
