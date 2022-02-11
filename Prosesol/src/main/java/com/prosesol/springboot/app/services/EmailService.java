package com.prosesol.springboot.app.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.mailjet.client.resource.Template;
import com.prosesol.springboot.app.util.Archivos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class EmailService {

	protected static final Log LOG = LogFactory.getLog(EmailService.class);
	
	@Value("${mail.jet.api.key}")
	private String mailJetApiKey;

	@Value("${mail.jet.secret.password}")
	private String mailJetSecretPassword;

	@Autowired
	private Archivos archivos;

	private MailjetClient client;

	private MailjetRequest request;

	private MailjetResponse response;

	/**
	 * Envia email MailJet
	 * 
	 * @throws MailjetException
	 * @throws MailjetSocketTimeoutException
	 * @throws IOException
	 */
	public void sendMailJet( Map<String, String> model, int idTemplate, List<String> correos, JSONArray beneficios,String valida )
			throws MailjetException, MailjetSocketTimeoutException, IOException {

		JSONArray arrayDatos = new JSONArray();
		JSONObject almacena = new JSONObject();
		String nombreD=null;
		client = new MailjetClient(mailJetApiKey, mailJetSecretPassword, new ClientOptions("v3.1"));
		request = new MailjetRequest(Emailv31.resource);
			JSONObject from = new JSONObject();
			if(valida == "true") {
				from.put("Email", "contacto@assismex.com");
				from.put("Name", "Assismex");
			}else {
				from.put("Email", "contacto@prosesol.org");
				from.put("Name", "Prosesol");
			}
			almacena.put(Emailv31.Message.FROM, from);
		// Parámetros dinámicos para el template
			JSONObject jsonObjectParameters = new JSONObject();
				for (Map.Entry<String, String> parametros : model.entrySet()) {
					jsonObjectParameters.put(parametros.getKey(), parametros.getValue());
					if(parametros.getKey().equals("afiliado")) {
						nombreD=parametros.getValue();
					}
				}
				jsonObjectParameters.put("beneficios", beneficios);
		// Arreglo para el envío de correos
			JSONObject to = new JSONObject();
			JSONArray toArray = new JSONArray();
				for (String correo : correos) {
					to.put("Email", correo);
					to.put("Name", nombreD);
				}
				
			toArray.put(to);
			almacena.put(Emailv31.Message.TO, toArray);
			almacena.put(Emailv31.Message.TEMPLATEID, idTemplate);
			almacena.put(Emailv31.Message.TEMPLATELANGUAGE, true);
			if(valida == "true") {
				almacena.put(Emailv31.Message.SUBJECT, "Bienvenido a Casa Salud");
			}else {
				almacena.put(Emailv31.Message.SUBJECT, "Prosesol");
			}
			//recibe errores de template por email
			/*almacena.put(Emailv31.Message.TEMPLATEERROR_DELIVERY, true);
            almacena.put(Emailv31.Message.TEMPLATEERROR_REPORTING, new JSONObject()
                    .put("Email", "alexander.garcia@gisconsultoria.com")
                    .put("Name", "recipient name"));*/
		
		almacena.put(Emailv31.Message.VARIABLES, jsonObjectParameters);

		arrayDatos.put(almacena);
		request.property(Emailv31.MESSAGES, arrayDatos);

		response = client.post(request);
		if(response.getStatus()==200) {
			LOG.info("El correo se envio correctamente: "+response.getData());
		}else {
			LOG.error("Error el correo no se pudo enviar: "+response.getData());
		}
		
	}

	public List getTemplateMailjet() throws MailjetException, MailjetSocketTimeoutException {
		List<Integer> templates = new ArrayList<>();
		client = new MailjetClient(mailJetApiKey, mailJetSecretPassword);
		// obtiene la lista de templates
		request = new MailjetRequest(Template.resource);
		response = client.get(request);
		for(int x=0; x < response.getData().length();x++) {
			Object obj = (Object) response.getData().get(x);
			JSONObject jo = (JSONObject) obj;
			templates.add(jo.getInt("ID"));
		}
		
		if(response.getStatus()==200) {
			LOG.info("Template obtenidos correctamente: "+response.getData());
		}else {
			LOG.error("Error al obtener los template: "+response.getData());
		}
		return templates;
	}
	
}
