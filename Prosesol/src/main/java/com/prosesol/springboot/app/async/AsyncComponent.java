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

}
