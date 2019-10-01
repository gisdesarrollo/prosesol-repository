package com.prosesol.springboot.app.util;

import org.springframework.stereotype.Service;

@Service
public class GenerarClave {

    private String clave;

    public String getClave(String clave){

        this.clave = "PR-";

        for (int i = 0; i < 10; i++) {
            this.clave += (clave.charAt((int) (Math.random() * clave.length())));
        }

        return this.clave;

    }
}
