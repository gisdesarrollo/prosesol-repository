package com.prosesol.springboot.app.async;

import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class AsyncComponent {

	private AsyncResult<String> result = new AsyncResult<String>("0 correos enviados");
	
	@Async
	public void sendMails(int totalMails) {
		for(int i = 1; i <= totalMails; i++) {
			try {
				sendMail(i);
				result = new AsyncResult<String>("Enviados " + i + " de " + totalMails);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}

	private void sendMail(int num) throws InterruptedException {
		Thread.sleep(1000);		
		System.out.println("Mail " + num + " enviado.");		
	}
	
	public AsyncResult<String> getResult() {
		return result;
	}

	public String getMailSender() throws ExecutionException {
		return result.get();
	}
}
