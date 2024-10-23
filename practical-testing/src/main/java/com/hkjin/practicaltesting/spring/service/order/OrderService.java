package com.hkjin.practicaltesting.spring.service.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hkjin.practicaltesting.spring.api.order.request.OrderCreateRequest;
import com.hkjin.practicaltesting.spring.api.order.response.OrderResponse;
import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	public OrderResponse createOrder (OrderCreateRequest orderCreateRequest, LocalDateTime registeredDateTime) {
		List<String> productNumbers = orderCreateRequest.getProductNumbers();
		// Product
		List<Product> productNumberIn = productRepository.findAllByProductNumberIn(productNumbers);

		// Order
		Order saved = orderRepository.save(Order.create(productNumberIn, registeredDateTime));

		return OrderResponse.of(saved);
	}

}
