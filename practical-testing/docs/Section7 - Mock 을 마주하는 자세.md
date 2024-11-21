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

## TestDouble [모킹 용어]
- 스턴트 맨(Stunt Double)이랑 비슷한? 그런 단어라고 생각하면 이해가 쉽다

https://www.martinfowler.com/bliki/TestDouble.html <br>

- Dummy -> 아무 것도 하지 않는 깡통 객체 (아무런 행위도 하지 않는다)
- Fake -> 단순한 형태로 동일한 기능은 수행하나, 프로덕션에서 쓰기에는 부족한 객체 
  - ex) FakeRepository
- Stub -> 테스트에서 요청한 것에 대해 미리 준비한 결과를 제공하는 객체, 그 외에는 응답하지 않는다.
- Spy -> stub 이면서 호출된 내용을 기록하여 보여줄 수 있는 개체
  - 일부는 실제 객체처럼 동작시키고 일부만 Stubbing 할 수 있다. (실제 객체랑 거의 유사하게 동작할 수 있다)
- Mock -> 행위에 대한 기대를 명세하고, 그에 따라 동작하도록 만들어진 객체
  
Stub 과 Mock 은 항상 헷갈리는 존재이다 <br>
Stub 은 '상태 검증' , Mock 은 '행위 검증' 을 하는편 이다 <br>


## 순수 Mockito로 검증해보기
#### [순수 Mockito 객체를 사용하여 테스트 코드 작성]
```java
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkjin.practicaltesting.spring.client.MailSendClient;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistory;
import com.hkjin.practicaltesting.spring.domian.history.MailSendHistoryRepository;

class MailServiceTest {

	/* @SpringBootTest 는 하지 않고, 진행함*/
	/* 순수 Mockito 에 대한 테스트를 한다. */

	/* Mock 객체는 아무것도 지정하지 않으면 예외가 발생하지 않고 기본값을 리턴한다. */

	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
	    // given

		// Mock 객체 생성
		// mock -> withSettings -> defaultAnswer 에 의해 객체 타입에 맞게 자동으로 반환을 해준다.
		MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
		MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

		MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

		// when
		Mockito.when(mailSendClient.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(true);

/*		Mockito.when(mailSendHistoryRepository.save(Mockito.any(MailSendHistory.class)))
			.thenReturn("");*/

		boolean result = mailService.sendMail("","","","");

		// then
		Assertions.assertThat(result).isTrue();

		/** Mockito 정확한 검증 */
		// Mockito 가 몇번 호출되었는지 검증한다.
		Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
	}

}
```
#### @Mock 을 사용한 테스트 코드 작성
```java
@ExtendWith(MockitoExtension.class) // Mockito 사용해서 Mock 만들거야 인지시키기!
class MailServiceTest {

	/* @SpringBootTest 는 하지 않고, 진행함*/
	/* 순수 Mockito 에 대한 테스트를 한다. */

	/* Mock 객체는 아무것도 지정하지 않으면 예외가 발생하지 않고 기본값을 리턴한다. */
	@Mock
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
	    // given

		// Mock 객체 생성
		MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

		// when
		Mockito.when(mailSendClient.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(true);

/*		Mockito.when(mailSendHistoryRepository.save(Mockito.any(MailSendHistory.class)))
			.thenReturn("");*/

		boolean result = mailService.sendMail("","","","");

		// then
		Assertions.assertThat(result).isTrue();

		/** Mockito 정확한 검증 */
		// Mockito 가 몇번 호출되었는지 검증한다.
		Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
	}

}

```

<br>

#### @Spy
@Spy 는 행위를 검증할 수 있다. Mockito 의 verify() 와 비슷한 느낌이다 <br>
한 메소드의 의존한 여러 메소드 중에서 내가 원하는 메소드만 stubbing 할 때 사용한다. <br>
하위 메소드들이 없어도 잘 동작하게 끔 만들기 위함 <br>

@Spy 는 Mockito.when 절을 사용하지 않는다.
왜냐하면 @Spy 는 실제 객체를 기반으로 만들어진다. 기존의 Mockito.when 은 모킹한 객체를 사용하기 때문에 테스트가 실패한다 <br>

한 객체에서 일부는 실제 객체의 기능을 사용하고 나머지 일부만 Stubbing 하고 싶을 때 @Spy 를 사용한다 <br>
사용하는 빈도는 엄청 많지는 않다 <br>

```java
@ExtendWith(MockitoExtension.class) 
class MailServiceTest {

	@Spy
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@Test
	@DisplayName("메일 전송 테스트")
	void sendMail() {
	    // given
		Mockito.doReturn(true)
			.when(mailSendClient)
			.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		// when
		boolean result = mailService.sendMail("","","","");

		// then
		Assertions.assertThat(result).isTrue();

		Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
	}

}
```

<br>

#### @InjectMocks
```java
@RequiredArgsConstructor
@Service
public class MailService {
	private final MailSendClient mailSendClient;
	private final MailSendHistoryRepository mailSendHistoryRepository;
        
	// 로직
	
}

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

  @Mock
  private MailSendClient mailSendClient;

  @Mock
  private MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  private MailService mailService;
```

@InjectMocks 를 통해 MailService 을 생성자를 보고 @Mock 이 선언된 객체들을 Inject 를 한다. <br>
위 어노테이션을 사용하지 않으면 
> 	MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

위 코드를 직접 작성해야 하는 번거로움이 매번있을 것이다 <br>

<br>

## BDDMockito






## Classicist VS Mockist







## 키워드 정리