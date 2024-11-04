package com.hkjin.practicaltesting.spring.api.order.response;

import java.time.LocalDateTime;
import java.util.List;

import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;
import com.hkjin.practicaltesting.spring.domian.order.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {
	private Long id;
	private int totalPrice;
	private LocalDateTime registeredDateTime;
	private List<ProductResponse> products;

	@Builder
	public OrderResponse (Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.registeredDateTime = registeredDateTime;
		this.products = products;
	}

	public static OrderResponse of (Order order) {
		return OrderResponse.builder()
			.id(order.getId())
			.totalPrice(order.getTotalPrice())
			.registeredDateTime(order.getRegisteredDateTime())
			.products(order.getOrderProducts().stream()
				.map(orderProduct -> ProductResponse.of(orderProduct.getProduct())).toList()
			)
			.build();
	}

}
