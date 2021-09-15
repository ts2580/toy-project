package com.kh.toy.common.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;

public class Mailsender {
	
	private static final Properties SMTP_PROPERTIES;
	
	static {
		
		SMTP_PROPERTIES = new Properties();
		SMTP_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
		SMTP_PROPERTIES.put("mail.smtp.port", "587");
		SMTP_PROPERTIES.put("mail.smtp.tls.enable", "true");
		SMTP_PROPERTIES.put("mail.smtp.starttls.enable", "true");
		SMTP_PROPERTIES.put("mail.smtp.ssl.protocols", "TLSv1.2");
		SMTP_PROPERTIES.put("mail.smtp.auth", "true");
		SMTP_PROPERTIES.put("mail.debug", "true");		
		
	}
	
	// 수신자, 메일 제목, 내용(body) 
	public void sendEmail(String to, String subject, String body) {

		Session session = Session.getInstance(SMTP_PROPERTIES, null);

		try {
			MimeMessage msg = setMessage(session, to, subject, body);
			sendMessage(session, msg);
		} catch (MessagingException mex) {
			
			// 사용자에게 "메일 발송중 에러" 전달 
			// log에 에러의 stack trace 전송
			throw new HandleableException(ErrorCode.MAIL_SEND_FAIL_EXCEPTION, mex);
		}

	}
	
	private MimeMessage setMessage(Session session, String to, String subject, String body) throws MessagingException {
		
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, to);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		msg.setText(body, "UTF-8", "HTML");
		return msg;
		
	}
	
	private void sendMessage(Session session, MimeMessage msg) throws MessagingException {
		Transport tr = session.getTransport("smtp");
		tr.connect("smtp.gmail.com", Config.SMTP_AUTHENTICATION_ID.DESC, Config.SMTP_PASSWORD.DESC);
		msg.saveChanges();
		tr.sendMessage(msg, msg.getAllRecipients());
		tr.close();
	}

}
