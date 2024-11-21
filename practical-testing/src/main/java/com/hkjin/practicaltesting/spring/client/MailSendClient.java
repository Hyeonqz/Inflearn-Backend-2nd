package com.hkjin.practicaltesting.spring.client;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailSendClient {

	public boolean sendEmail (String fromEmail, String toEmail, String subject, String content) {
		log.info("[메일 전송]");
		throw new IllegalArgumentException("메일 전송");
	}

	public void hi() {
		log.info("hi");
	}

	public void bye() {
		log.info("bye");
	}

	public void eat() {
		log.info("eat");
	}
}
