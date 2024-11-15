package com.hkjin.practicaltesting.spring.api.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.order.OrderStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	public boolean sendOrderStatisticsMail (LocalDate orderDate, String email) {
		// 해당 일자에 결제완료된 주문들을 가져온다
		List<Order> orders = orderRepository.findOrdersBy(orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(), OrderStatus.PAYMENT_COMPLETED);

		// 총 매출 합계를 계산한다
		int sum = orders.stream()
			.mapToInt(Order::getTotalPrice)
			.sum();

		// 메일 전송
		boolean result = mailService.sendMail(
			"no-reply@cafeKiosk.com",
			email,
			String.format("%s[매출통계]", orderDate),
			String.format("총 매출 합계는 %s 원 입니다.", sum)
		);

		if(!result) {
			throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다");
		}
		return true;
	}

}
