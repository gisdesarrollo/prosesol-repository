package com.prosesol.springboot.app.async;

import com.prosesol.springboot.app.exception.CustomAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@ComponentScan("com.prosesol.springboot.app.async")
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor(){
	    return new ThreadPoolTaskExecutor();

    }

    @Bean(name = "threadCargaMasiva")
    public Executor threadCargaMasiva(){
	    return new ThreadPoolTaskExecutor();
    }

    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
