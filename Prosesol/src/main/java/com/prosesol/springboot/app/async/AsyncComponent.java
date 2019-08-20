package com.prosesol.springboot.app.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AsyncComponent {

	@Async("threadPoolTaskExecutor")
	public void asyncMethodVoidReturnType(boolean isValid) {

		if (isValid) {
			System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
		} else {
			throw new IllegalStateException("Invalid");
		}
	}

	@Async("threadCargaMasiva")
	public void asyncCargaMasiva(byte[] bs) throws InterruptedException {
		Thread.sleep(10000);

		System.out.println("Entra al método para la lectura de archvio");
		System.out.println(new String(bs));
	}
}
