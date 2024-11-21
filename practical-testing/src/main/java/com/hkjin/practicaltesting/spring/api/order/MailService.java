package com.hkjin.practicaltesting.spring.api.order;

import org.springframework.stereotype.Service;

import com.hkjin.practicaltesting.spring.client.MailSendClient;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistory;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistoryRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailService {
	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryRepository;

	public boolean sendMail (String fromEmail, String toEmail, String subject, String content) {
		boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);
		if(result) {
			mailSendHistoryRepository.save(MailSendHistory.builder()
				.fromEmail(fromEmail)
				.toEmail(toEmail)
				.subject(subject)
				.content(content)
				.build()
			);
			mailSendClient.hi();
			mailSendClient.bye();
			mailSendClient.eat();

			return true;
		} else {
			return false;
		}
	}

}
