package com.prosesol.springboot.app.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsynExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.out.println("Exception message: " + throwable.getStackTrace().toString());
        System.out.println("Method name: " + method.getName());
        for(final Object parametros : obj){
            System.out.println("Params: " + parametros);
        }
    }
}
