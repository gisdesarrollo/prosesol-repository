package com.prosesol.springboot.app.service;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.mail.SimpleMailMessage;

import com.prosesol.springboot.app.util.Mail;


public interface IEmailService {

//	void sendText(String from, String to, String subject, String body);
//	
//	void sendHTML(String from, String to, String subject, String body);
	
//	void sendSimpleEmail(String from, String to, String subject, String body);
//	
//	void senSimpleMessageUsingTemplate(String from, String to, String subject, SimpleMailMessage template, String body, 
//									   String[] templateArgs);
//	
//	void sendMessageWithAttachment(String from, String to, String subject, String text, String pathToAttachment);
	
	void sendSimpleMessage(Mail mail) throws MessagingException, IOException;
	
	
}
