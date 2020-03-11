package com.prosesol.springboot.app.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    protected final Log LOG = LogFactory.getLog(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        for(StackTraceElement stackTraceElement : throwable.getStackTrace()){
            LOG.info("Exception message: " + stackTraceElement.toString());
        }

        LOG.info("Method name: " + method.getName());

        for(final Object parametros : obj){
            LOG.info("Params: " + parametros);
        }
    }
}
