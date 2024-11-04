# Section6 Presentation Layer Test 
- 3주차 회고
  - https://www.inflearn.com/blogs/8882
- 4주차 회고
  - https://www.inflearn.com/blogs/9081
### Presentation Test
- 외부 세계의 요청을 가장 먼저 받는 계층 -> Controller
- 파라미터에 대한 최소한이 검증을 수행한다
  - 비즈니스 로직은 Service Layer 에서 검증을 한다.
  - ⭐️ 즉 비즈니스 로직을 타기전에 클라이언트에서 요청한 값이 유효한지를 검증 하는 것이 제일 중요하다고 생각한다.

Presentation Layer 를 테스트 할 떄는 보통 Service, Persistence 를 직접 호출하지 않고 Mocking 을 하여 진행한다 <br>
Mocking 은 가짜 객체를 의미한다. 즉 정상 동작한다는 것을 가정하여 사용하는 것이다 <br>
-> Service 에 어떠한 메소드가 있고 그 메소드를 컨트롤러에서 사용한다고 하면, 그 메소드는 무조건 맞다는 것을 가정하여 테스트를 진행한다는 것이다 <br>

#### [Mock]
- @MockMvc
  - Mock 객체를 사용해 스프링 MVC 동작을 재현할 수 있는 테스트 프레임워크 이다.

#### 새로운 요구사항
- 관리자 페이지에서 신규 상품을 등록할 수 있다.
- 상품명,상품 타입, 판매 상태, 가격 등을 입력 받는다.

```java
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void tearDown () {
		productRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("신규상품을 등록한다, 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
	void createProduct () {
		// given
		Product product = createProduct("001", HANDMADE, SELLING, 4000, "아메리카노");
		ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카페라떼")package com.hkjin.practicaltesting.spring.service.product;

import static com.hkjin.practicaltesting.spring.domian.product.ProductSellingType.*;
import static com.hkjin.practicaltesting.spring.domian.product.ProductType.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.hkjin.practicaltesting.spring.api.product.dtos.request.ProductCreateRequest;
import com.hkjin.practicaltesting.spring.api.product.dtos.response.ProductResponse;
import com.hkjin.practicaltesting.spring.domian.product.Product;
import com.hkjin.practicaltesting.spring.domian.product.ProductRepository;
import com.hkjin.practicaltesting.spring.domian.product.ProductSellingType;
import com.hkjin.practicaltesting.spring.domian.product.ProductType;

      @ActiveProfiles("test")
      @SpringBootTest
      class ProductServiceTest {

        @Autowired
        private ProductService productService;

        @Autowired
        private ProductRepository productRepository;

        @AfterEach
        void tearDown () {
          productRepository.deleteAllInBatch();
        }

        @Test
        @DisplayName("신규상품을 등록한다, 상품 번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
        void createProduct () {
          // given
          Product product = createProduct("001", HANDMADE, SELLING, 4000, "아메리카노");
          ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                  .type(HANDMADE)
                  .sellingType(SELLING)
                  .name("카페라떼")
                  .price(5000)
                  .build();

          // when
          ProductResponse productResponse = productService.createProduct(productCreateRequest);

          // then
          List<Product> products = productRepository.findAll();
          Assertions.assertThat(productResponse)
                  .extracting("productNumber", "type", "sellingType", "name", "price")
                  .containsExactlyInAnyOrder(
                          Tuple.tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                          Tuple.tuple("002",HANDMADE,SELLING,"카페라떼",5000)
                  );

        }

        @Test
        @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
        void ProductServiceTest () {
          // given
          ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                  .type(HANDMADE)
                  .sellingType(SELLING)
                  .name("카푸치노")
                  .price(5000)
                  .build();

          // when
          ProductResponse productResponse = productService.createProduct(productCreateRequest);

          // then
          Assertions.assertThat(productResponse.getProductNumber()).isEqualTo("001");
        }

        private Product createProduct (String productNumber, ProductType type, ProductSellingType sellingType, int price,
                String name) {
          return Product.builder()
                  .productNumber(productNumber)
                  .type(type)
                  .sellingType(sellingType)
                  .price(price)
                  .name(name)
                  .build();
        }

      }
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(productCreateRequest);

		// then
		List<Product> products = productRepository.findAll();
		Assertions.assertThat(productResponse)
			.extracting("productNumber", "type", "sellingType", "name", "price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
				Tuple.tuple("002",HANDMADE,SELLING,"카페라떼",5000)
			);

	}

	@Test
	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
	void ProductServiceTest () {
		// given
		ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingType(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		ProductResponse productResponse = productService.createProduct(productCreateRequest);

		// then
		Assertions.assertThat(productResponse.getProductNumber()).isEqualTo("001");
	}

	private Product createProduct (String productNumber, ProductType type, ProductSellingType sellingType, int price,
		String name) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingType(sellingType)
			.price(price)
			.name(name)
			.build();
	}

}
```

만약 위 상단에 @Transactional(readOnly=true) 를 테스트에서 주게 된다면? <br>
기본은 false 이다. true 값은 읽기 전용 트랜잭션이 열린다는 뜻이다. <br>

읽기 전용 Transaction 은 CUD 작업이 동작을 하지않고 select 만 가능하다 <br>
JPA 에서는 엔티티를 1차 캐쉬 트랜잭션 매니저가 영속성 컨텍스트에 저장을 하고, 트랜잭션이 commit,flush 하는 시점에 변경감지를 한다 <br>
달라진 것이 있으면 더티체킹을 하여 변경을 감지한다 <br>

하지만 readonly=true 를 하면 변경감지(더티체킹) 을 하지 않는다, 그래서 성능에도 도움이 된다 <br>
그리고 CQRS - Command(CUD) / Read(R) 를 분리를 하자 <br>
보통 cud 보다 read 가 대부분 더 많다 <br>
cqrs -> command 와 read 의 책임을 분리하여 연관이 없게끔 하자 <br>

Read 형 DB, Write 형 DB 를 나눌 수 있다 -> Master(write)/ Slave(read) 전용으로 사용을 하는 추세이다 <br>
readonly=true 가 걸려져있으면 Slave DB 를 사용하게 끔 로직을 만들고, true 가 없으면 Master DB 를 사용하게 끔하자 <br>

Master 는 Production 환경에서 계속 살아 있어야 하기 때문에 상대적으로 요청이 적은 CUD 작업을 위주로 맡고 <br>
Slave 는 Master DB 에 CUD 작업을 바로바로 동기화 시키고 그에 대한 내용을 Select 작업만 할 수 있게 한다 <br>

보통 서비스 최상단에 @Transactional(readOnly=true) 를 걸고, CUD 작업이 있는 메소드에 @Transactional 을 건다 <br>