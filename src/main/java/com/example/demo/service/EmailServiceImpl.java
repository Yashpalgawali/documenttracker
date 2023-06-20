package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailserv")
public class EmailServiceImpl implements EmailService {

	
	@Autowired
	JavaMailSender mailSend ;
	
	@Override
	public void sendSimpleMail(String tomail, String body, String subject) {
		// TODO Auto-generated method stub

		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setTo(tomail);
		mail.setText(body);
		mail.setSubject(subject);
		
		mailSend.send(mail);
		
		System.err.println("Mail is sent");
		
	}

}
