package com.cubit.celerity.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cubit.celerity.util.dto.Mail;

public class SendMail {
			
	@SuppressWarnings("finally")
	public Boolean sendOneHTML(Mail mail) {
		Boolean result = false;
		try {
			Message message = new MimeMessage(this.getSession());
			message.setFrom(new InternetAddress(Constants.ACCOUNT_SEND_EMAIL));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getAddress()));
			message.setHeader("Content-Type", "text/html; charset=UTF-8"); //ISO-8859-1
			message.setSubject(mail.getSubject());
			message.setContent(mail.getBody(), "text/html; charset=utf-8");
			Transport.send(message);
			result = true;
		} catch (MessagingException e) {
			System.out.println(e.toString()); //throw new RuntimeException(e);
		} finally {
			return result;
		}
	}
	
	@SuppressWarnings("finally")
	public Boolean sendOne(Mail mail) {
		Boolean result = false;
		try {
			Message message = new MimeMessage(this.getSession());
			message.setFrom(new InternetAddress(Constants.ACCOUNT_SEND_EMAIL));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getAddress()));
			message.setHeader("Content-Type", "text/plain; charset=UTF-8"); //ISO-8859-1
			message.setSubject(mail.getSubject());
			message.setText(mail.getBody());
			Transport.send(message);
			result = true;
		} catch (MessagingException e) {
			System.out.println(e.toString()); //throw new RuntimeException(e);
		} finally {
			return result;
		}
	}
	
	private Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", Constants.SMTP_MAIL_AUTH);
		props.put("mail.smtp.starttls.enable", Constants.SMTP_MAIL_TLS);
		props.put("mail.smtp.host", Constants.SMTP_MAIL_HOST);
		props.put("mail.smtp.port", Constants.SMTP_MAIL_PORT);
		props.put("mail.mime.charset", "UTF-8"); //ISO-8859-1
		return props;
	}

	private Session getSession() {
		Properties properties = this.getProperties();
		if (Constants.SMTP_MAIL_AUTH.compareToIgnoreCase("true") == 0) {
			return Session.getInstance(properties, 
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(Constants.ACCOUNT_SEND_EMAIL, Constants.ACCOUNT_SEND_PASSWORD);
						}
				   	});
		} else {
			return Session.getInstance(properties);
		}
	}
	
}
