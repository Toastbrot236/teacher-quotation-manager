package service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailSender {
	
	private static final String USERNAME = "CENSORED";
	private static final String PASSWORD = "CENSORED";
	
	private static final String FROM_EMAIL = "CENSORED";
	
	public static boolean sendMail(String subject, String text, String to) {
		
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", Boolean.TRUE);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "25");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Authenticator auth = new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		};
		
		Session session = Session.getInstance(prop, auth);
		
		try {
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_EMAIL));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(to)
					);
			message.setSubject(subject);
			
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(text, "text/html; charset=utf-8");
			
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(mimeBodyPart);
			
			message.setContent(mimeMultipart);
			
			Transport.send(message);
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	
}
