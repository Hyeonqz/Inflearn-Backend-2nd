package com.hkjin.practicaltesting.spring.domian.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

class OrderTest {

	@Test
	@DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
	void calculateTotalPrice () {
		// given
		List<Product> product = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000),
			createProduct("003", 3000)
		);

		// when
		Order order = Order.create(product, LocalDateTime.now());

		// then
		assertThat(order.getTotalPrice()).isEqualTo(6000);
	}

	@Test
	@DisplayName("주문 생성 시 주문 상태는 INIT 이다.")
	void OrderTest() {
		// given
		List<Product> product = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000),
			createProduct("003", 3000)
		);

		// when
		Order order = Order.create(product, LocalDateTime.now());

		assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
	}

	@Test
	@DisplayName("주문 생성 시 주문 등록 시간을 기록한다")
	void registeredDateTime() {
		// given
		LocalDateTime now = LocalDateTime.now();
		List<Product> product = List.of(
			createProduct("001", 1000),
			createProduct("002", 2000),
			createProduct("003", 3000)
		);

		// when
		Order order = Order.create(product, now);

		assertThat(order.getRegisteredDateTime()).isEqualTo(now);
	}

	private Product createProduct (String productNumber, int price) {
		return Product.builder()
			.type(ProductType.HANDMADE)
			.productNumber(productNumber)
			.sellingType(ProductSellingType.SELLING)
			.price(price)
			.name("메뉴 이름")
			.build();
	}

}