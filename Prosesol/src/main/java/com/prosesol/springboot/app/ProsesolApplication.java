package com.prosesol.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class ProsesolApplication{

	@Autowired 
	BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ProsesolApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		String password = "12345";
//		for(int i = 0; i < 2; i++) {
//			String bCryptPassword = passwordEncoder.encode(password);
//			System.out.println(bCryptPassword);
//		}
//		
//	}

	
	
}
