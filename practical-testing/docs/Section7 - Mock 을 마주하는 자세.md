# Section7 - Mock 을 마주하는 자세
## Mockito 로 Stubbing 하기
```java
package com.hkjin.practicaltesting.spring.service.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.hkjin.practicaltesting.PracticalTestingApplication;
import com.hkjin.practicaltesting.spring.api.order.OrderStatisticsService;
import com.hkjin.practicaltesting.spring.client.MailSendClient;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistory;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistoryRepository;
import com.hkjin.practicaltesting.spring.domian.order.Order;
import com.hkjin.practicaltesting.spring.domian.order.OrderRepository;
import com.hkjin.practicaltesting.spring.domian.order.OrderStatus;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest(classes = PracticalTestingApplication.class)
public class OrderStaticServiceTest {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@AfterEach
	void tearDown () {
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}
	
	// 가짜 객체를 만든다
	@MockBean
	private MailSendClient mailSendClient;
	// Mock 을 통하여 가짜객체가 어떻게 동작하면 좋겠고, 어떤 행동을 했을 때 어떤 return 이 있으면 좋을지 고민

	@Test
	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다")
	void sendOrderStatisticsMail() {
	    // given
		LocalDateTime now = LocalDateTime.of(2023,3,5,0,0);

		Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
		Product product2 = createProduct(ProductType.HANDMADE, "002", 1000);
		Product product3 = createProduct(ProductType.HANDMADE, "003", 1000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		// 경계값 테스트를 하기 위한 객체들 값과 관련된 테스트는 !경계값 테스트! 를 자주 해보자.
		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,4,23,59));
		Order order2 = createPaymentCompletedOrder(products, now);
		Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,5,23,59));
		Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023,3,6,0,0));

	    // when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@naver.com");

		Mockito.when(mailSendClient.sendEmail(
			ArgumentMatchers.any(String.class), // String 값을 어느 값이든 상관없다는 뜻
			ArgumentMatchers.any(String.class),
			ArgumentMatchers.any(String.class),
			ArgumentMatchers.any(String.class)
		)).thenReturn(true);

		// then
		Assertions.assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		Assertions.assertThat(histories).isNotEmpty();
		Assertions.assertThat(histories).hasSize(1)
			.extracting("content")
			.contains("총 매출 합계는 12000원 입니다.")
			;
	}

	private Order createPaymentCompletedOrder (List<Product> products,LocalDateTime now) {
		 Order order = Order.builder()
			.products(products)
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registeredDateTime(now)
			.build();
		 return orderRepository.save(order);
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

```

```java

// 가짜 객체를 만든다
@MockBean
private MailSendClient mailSendClient;
// Mock 을 통하여 가짜객체가 어떻게 동작하면 좋겠고, 어떤 행동을 했을 때 어떤 return 이 있으면 좋을지 고민

		Mockito.when(mailSendClient.sendEmail(
			ArgumentMatchers.any(String.class), // String 값을 어느 값이든 상관없다는 뜻
			ArgumentMatchers.any(String.class),
			ArgumentMatchers.any(String.class),
			ArgumentMatchers.any(String.class)
		)).thenReturn(true);
```

위 코드를 알고있는 것이 좋다 <br>
위 가짜 객체를 만들어 위 메소드의 동작을 정의할 수 있다 <br>
> Mock 객체에 원하는 행위를 정의한다
>> '**Stubbing'** 한다 라고 부른다 <br>

추가적인 팁으로 메일전송 로직에는 @Transactional 을 붙이지 않는게 좋다 <br>
@Transactional 이 걸려있으면 메소드가 끝날 때 까지 DB 에 Connection 을 가지고 있다 <br>

메일 작업 처럼 무언가 긴 작업이 있는 메소드면 실제로 트랜잭션에는 참여하지 않아도 된다면 @Transactional 은 지양하자 <br>
간단한 조회를 할 때는 조회 전용 @Transactional 이 따로 걸리기 때문에 상황을 봐가면서 잘 사용해야 한다 <br>

## TestDouble




## @Mock @Spy @InjectMocks




## BDDMockito



## Classicist VS Mockist



## 키워드 정리