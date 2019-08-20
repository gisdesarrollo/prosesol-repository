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
	public void sendAsynchronousMails() throws InterruptedException {
		System.out.println("Starts - invoking an asynchronous method " + Thread.currentThread().getName());
		asyncComponent.asyncMethodVoidReturnType(true);
		System.out.println("End - invoking an asynchronous method. " + Thread.currentThread().getName());
		Thread.sleep(250);
	}

	@Test
	public void sendAsynchronousMailsThrowingException() throws InterruptedException {
		System.out.println("Starts - invoking an asynchronous method to throw Exception" + Thread.currentThread().getName());
		asyncComponent.asyncMethodVoidReturnType(false);
		Thread.sleep(10000);
	}

}
