package com.hkjin.practicaltesting.spring.service.order;

import static com.hkjin.practicaltesting.spring.domian.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkjin.practicaltesting.spring.IntegrationTestSupport;
import com.hkjin.practicaltesting.spring.api.order.request.OrderCreateRequest;
import com.hkjin.practicaltesting.spring.api.order.response.OrderResponse;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.orderproduct.OrderProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;
import com.hkjin.practicaltesting.spring.domian.stock.Stock;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.stock.StockRepository;

class OrderServiceTest extends IntegrationTestSupport {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	void setUp() {
		// bean 이 instance 주입을 해주기 때문에 초기화할 필요가 없다.
	}

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
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

	@Test
	@DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
	void createOrderWithStock() {
		LocalDateTime now = LocalDateTime.now();
	    // given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		productRepository.saveAll(List.of(product1,product2,product3));

		Stock stock = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock,stock2));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001","002","003"))
			.build();

		// when
		OrderResponse orderResponse = orderService.createOrder(request, now);

	    // then
		assertThat(orderResponse.getId()).isNotNull();
		assertThat(orderResponse)
			.extracting("registeredDateTime", "totalPrice")
			.contains(now, 7000);

		assertThat(orderResponse.getProducts()).hasSize(4)
			.extracting("productNumber","price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001",1000),
				Tuple.tuple("001",1000),
				Tuple.tuple("002",2000),
				Tuple.tuple("003",3000)
			);

		List<Stock> stocks = stockRepository.findAll();

		assertThat(stocks).hasSize(2)
			.extracting("productNumber","quantity")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001",0),
				Tuple.tuple("002",1)
			);
	}

	@Test
	@DisplayName("재고와 부족한 상품으로주문을 생성하려는 경우 예외가 발생한다")
	void createOrderWithNoStock() {
		LocalDateTime now = LocalDateTime.now();
		// given
		Product product1 = createProduct(BOTTLE, "001", 1000);
		Product product2 = createProduct(BAKERY, "002", 2000);
		Product product3 = createProduct(HANDMADE, "003", 3000);
		productRepository.saveAll(List.of(product1,product2,product3));

		Stock stock = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stock.deductQuantity(1); // TODO
		stockRepository.saveAll(List.of(stock,stock2));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "001","002","003"))
			.build();

		// when / then
		assertThatThrownBy(() -> orderService.createOrder(request, now))
			.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("재고가 부족한 상품이 있습니다");

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