package com.hkjin.practicaltesting.spring.api.order;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown () {
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송합니다.")
	void sendOrderStatisticsMail() {
	    // given
		LocalDateTime now = LocalDateTime.of(2023,3,5,10,0);
		Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
		Product product2 = createProduct(ProductType.HANDMADE, "002", 2000);
		Product product3 = createProduct(ProductType.HANDMADE, "003", 3000);
		productRepository.saveAll(List.of(product1,product2,product3));

	    // when
		Order.create(List.of(product1,product2,product3), now);




	    // then
	}

	private Product createProduct (ProductType type, String productNumber, int price) {
		return Product.builder()
			.type(type)
			.productNumber(productNumber)
			.sellingType(ProductSellingType.SELLING)
			.price(price)
			.name("메뉴 이름")
			.build();
	}


}