package com.hkjin.practicaltesting.spring.api.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hkjin.practicaltesting.spring.api.order.request.OrderCreateRequest;
import com.hkjin.practicaltesting.spring.service.order.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {
	private final OrderService orderservice;

	@PostMapping("/api/v1/orders/new")
	public void createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
		LocalDateTime now = LocalDateTime.now();
		orderservice.createOrder(orderCreateRequest, now);
	}
}
