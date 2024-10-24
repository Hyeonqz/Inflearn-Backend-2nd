package com.hkjin.practicaltesting.spring.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

		List<Product> duplicateProducts = findProductsBy(productNumbers);

		Order saved = orderRepository.save(Order.create(duplicateProducts, registeredDateTime));

		return OrderResponse.of(saved);
	}

	private List<Product> findProductsBy (List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream()
			.map(productMap::get)
			.toList();
	}

}
