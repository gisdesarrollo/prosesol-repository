package com.prosesol.springboot.app.service;

import java.io.IOException;

import javax.mail.MessagingException;

import com.prosesol.springboot.app.util.Mail;


public interface IEmailService {
	
	void sendSimpleMessage(Mail mail, String bandera) throws MessagingException, IOException;
	
	
}
