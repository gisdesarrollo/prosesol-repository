package com.prosesol.springboot.app.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        for(StackTraceElement stackTraceElement : throwable.getStackTrace()){
            System.out.println("Exception message: " + stackTraceElement.toString());
        }
        System.out.println("Method name: " + method.getName());
        for(final Object parametros : obj){
            System.out.println("Params: " + parametros);
        }
    }
}
