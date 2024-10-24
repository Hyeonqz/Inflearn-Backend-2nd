package com.hkjin.practicaltesting.spring.service.order;

import static com.hkjin.practicaltesting.spring.domian.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.hkjin.practicaltesting.spring.api.order.request.OrderCreateRequest;
import com.hkjin.practicaltesting.spring.api.order.response.OrderResponse;
import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.orderproduct.OrderProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;
import com.hkjin.practicaltesting.spring.repository.ProductRepository;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	void createOrder () {
		// given
		LocalDateTime now = LocalDateTime.now();
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, now);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(now, 4000);

		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("002", 3000)
			);

	}

	@Test
	@DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
	void createOrderWithDuplicateProducts() {
		LocalDateTime now = LocalDateTime.now();
		Product product1 = createProduct(HANDMADE, "001", 1000);
		Product product2 = createProduct(HANDMADE, "002", 3000);
		Product product3 = createProduct(HANDMADE, "003", 5000);
		productRepository.saveAll(List.of(product1, product2, product3));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, now);

		// then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(now, 2000);

		assertThat(orderResponse.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 1000),
				tuple("001", 1000)
			);
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