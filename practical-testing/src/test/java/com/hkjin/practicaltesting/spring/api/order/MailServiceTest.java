package com.hkjin.practicaltesting.spring.api.order;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkjin.practicaltesting.spring.client.MailSendClient;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistory;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class) // Mockito 사용해서 Mock 만들거야 인지시키기!
class MailServiceTest {

	/* @SpringBootTest 는 하지 않고, 진행함*/
	/* 순수 Mockito 에 대한 테스트를 한다. */

	/* Mock 객체는 아무것도 지정하지 않으면 예외가 발생하지 않고 기본값을 리턴한다. */
	@Spy
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
	    // given
		Mockito.doReturn(true)
			.when(mailSendClient)
			.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		// when
		boolean result = mailService.sendMail("","","","");

		// then
		Assertions.assertThat(result).isTrue();

		Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
	}

}