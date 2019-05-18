package com.prosesol.springboot.app.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.prosesol.springboot.app.entity.Correo;
import com.prosesol.springboot.app.util.Mail;

@Service
public class EmailServiceImpl implements IEmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static String html;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private CorreoServiceImpl correoService;
	
	@Autowired
	private AdjuntoServiceImpl adjuntoService;

	@Override
	public void sendSimpleMessage(Mail mail, String bandera) throws MessagingException, IOException {
		
		Correo correo = new Correo();

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(mail.getModel());

		switch (bandera) {
		case "suspension":

			correo = correoService.getTemplateCorreoByName(bandera);
			html = correo.getHtml();
			break;
		case "inscripcion":
			logger.info("Template de inscripcion");
			
			correo = correoService.getTemplateCorreoByName(bandera);
//			adjuntoService.getAdjuntoCorreo(correo.getId());
			
			html = correo.getHtml();
			System.out.println(html);
			
//			html = this.templateEngine.process("/mail/inscripcion_template", context);
			break;
		case "aviso":
			logger.info("Template de aviso");
			
			correo = correoService.getTemplateCorreoByName(bandera);
			html = correo.getHtml();
			break;
		}

		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());
		
		

		helper.addInline("suspensionHeader", new ClassPathResource("static/img/logos/suspension_header.jpg"),
				"image/jpg");
		helper.addInline("suspensionFooter", new ClassPathResource("static/img/logos/suspension_footer.png"),
				"image/png");

		javaMailSender.send(message);
	}

}
