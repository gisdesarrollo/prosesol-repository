package com.prosesol.springboot.app.async;

import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AsyncConfig.class}, loader = AnnotationConfigContextLoader.class)
public class AsyncAnnotationExampleIntegrationTest {

	@Autowired
	private AsyncComponent asyncComponent;
	
	@Test
	public void sendAsynchronousMails() throws ExecutionException {
		asyncComponent.sendMails(100);
		System.out.println(asyncComponent.getMailSender());
		
		sleepALittle(10000);
		
		System.out.println(asyncComponent.getMailSender());
		
//		stopSendMails();
	}

//	private void stopSendMails() {
//		asyncComponent.getResult().cancel(true);	
//	}

	private void sleepALittle(int time) {
		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
