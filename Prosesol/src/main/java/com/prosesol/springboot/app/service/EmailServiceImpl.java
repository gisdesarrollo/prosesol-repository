package com.prosesol.springboot.app.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.prosesol.springboot.app.util.Mail;

@Service
public class EmailServiceImpl implements IEmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static String html;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public void sendSimpleMessage(Mail mail, String bandera) throws MessagingException, IOException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(mail.getModel());

		switch (bandera) {
		case "suspension":

			logger.info("Template de suspensión");

			html = this.templateEngine.process("/mail/suspension-template", context);
			break;
		case "inscripcion":
			logger.info("Template de inscripcion");
			
			html = this.templateEngine.process("/mail/inscripcion_template", context);
			break;
		case "aviso":
			logger.info("Template de aviso");
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
