package com.prosesol.springboot.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.prosesol.springboot.app.util.Mail;


@Service
public class EmailServiceImpl implements IEmailService{
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Override
	public void sendSimpleMessage(Mail mail) throws MessagingException, IOException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,
									MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
									StandardCharsets.UTF_8.name());
		
		helper.addAttachment("logo.png", new ClassPathResource("/static/img/logos/memorynotfound-logo.png"));
		
		Context context = new Context();
		context.setVariables(mail.getModel());
		
		String html = this.templateEngine.process("email-template", context);
		
		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		
		javaMailSender.send(message);
	}
	
//	/**
//	 * @author Kikin
//	 * Envío de correos personalizado
//	 */
//	
//	@Override
//	public void sendSimpleEmail(String from, String to, String subject, String body) {
//
//		try {
//			
//			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//			simpleMailMessage.setFrom(from);
//			simpleMailMessage.setTo(to);
//			simpleMailMessage.setSubject(subject);
//			simpleMailMessage.setText(body);
//			
//			javaMailSender.send(simpleMailMessage);
//			
//		}catch(MailException exc) {
//			logger.error("Error al momento de enviar el correo: " , exc);
//		}
//		
//	}
//
//	@Override
//	public void senSimpleMessageUsingTemplate(String from, String to, String subject, SimpleMailMessage template, String body,
//			String[] templateArgs) {
//		
//		String text = String.format(template.getText(), templateArgs);
//		
//		sendSimpleEmail(from, to, subject, body);
//		
//	}
//
//	@Override
//	public void sendMessageWithAttachment(String from, String to, String subject, String text, String pathToAttachment) {
//		
//		try {
//			
//			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//			
//			mimeMessageHelper.setFrom(from);
//			mimeMessageHelper.setSubject(subject);
//			mimeMessageHelper.setText(text);
//			
//			FileSystemResource file = new FileSystemResource(new File(""));
//			
//			mimeMessageHelper.addAttachment("Invoice", file);
//			
//		}catch(MessagingException e) {
//			logger.error("Error al enviar correo: ", e);
//		}
//		
//	}
	
	/**
	 * Sending email using send grid
	 */
	
//	@Value("${spring.sendgrid.api-key}")
//	String sendGridAPIKey;
//	
//	private SendGrid sendGridClient;
//	
//	@Autowired
//	public EmailServiceImpl(SendGrid sendGridClient) {
//		this.sendGridClient = sendGridClient;
//	}
//
//	@Override
//	public void sendText(String from, String to, String subject, String body) {
//
//		Response response = sendEmail(from, to, subject, new Content("text/plain", body));
//		System.out.println("Status Code: " + response.getStatusCode() + ", Body: "
//				+ response.getBody() + response.getHeaders());
//		
//	}
//
//	@Override
//	public void sendHTML(String from, String to, String subject, String body) {
//
//		Response response = sendEmail(from, to, subject, new Content("text/html", body));
//		System.out.println("Status Code: " + response.getStatusCode() + ", Body: "
//				+ response.getBody() + response.getHeaders());
//		
//	} 
//	
//	private Response sendEmail(String from, String to, String subject, Content content) {
//		
//		Mail mail = new Mail(new Email(from), subject, new Email(to), content);
//		
//		sendGridClient = new SendGrid(sendGridAPIKey);	
//		System.out.println(sendGridClient.toString());
//		
//		Request request = new Request();		
//		Response response = null;
//		
//		try {
//			request.setMethod(Method.POST);
//			request.setEndpoint("mail/send");
//			request.setBody(mail.build());
//			response = sendGridClient.api(request);
//			
//		}catch(IOException ex) {
//			System.out.println(ex.getMessage());
//		}
//		
//		return response;
//	}
	
}
