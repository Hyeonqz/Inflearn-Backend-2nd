# Section6 Business Layer Test 
#### Persistence Layer
- Data Access 의 역할
- 비즈니스 가공 로직이 포함되어서는 안 된다.
  - Data 에 대한 CRUD 에만 집중한 레이어여야 한다.

즉 Repository Test 이고, 통합테스트 즉 서버(=Spring) 을 띄워서 테스트를 진행한다 <br>

### Business Layer
- 비즈니스 로직을 구현하는 역할 -> Service Layer
- Persistence Layer 와 상호작용을 통해 비즈니스 로직을 전개시킨다.
- **트랜잭션**을 보장해야 한다.

Business Layer Test 는 Business + Persistence Layer 2개를 통합적으로 동작을 하는지 테스트를 해야 한다 <br>

[요구사항]
- 상품 번호 리스트를 받아 주문 생성하기
- 주문은 주문 상태, 주문 등록 시간을 가진다.
- 주문의 총 금액을 계산할 수 있어야 한다.

[Business Layer Test(=Service Layer Test)]
```java
@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
	void createOrder () {
		// given
		Product product = createProduct(ProductType.HANDMADE, "001", 10000);
		Product product1 = createProduct(ProductType.HANDMADE, "002", 9000);
		Product product2 = createProduct(ProductType.HANDMADE, "003", 8000);

		productRepository.saveAll(List.of(product, product1, product2));

		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		// when
		OrderResponse response = orderService.createOrder(request);

		// then
		assertThat(response.getId()).isNotNull();
		assertThat(response)
			.extracting("registeredDateTime","totalPrice")
				.contains(LocalDateTime.now(), 10000);

		assertThat(response.getProducts()).hasSize(2)
			.extracting("productNumber","price")
			.containsExactlyInAnyOrder(
				Tuple.tuple("001",10000),
				Tuple.tuple("002",9000)
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
```

- @DataJpaTest 로 진행할 경우 jpa 관련 빈만 찾아와서 bean 을 찾지 못하는 오류가 생길 수 있다.
  - 그러므로 Business Layer 에서는 @SpringBootTest 를 사용하여 전체 Bean 을 사용할 수 있도록 한다.

**[Enum 값 그 자체를 비교해주는 메소드이다.]**
> assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);

테스트 간 서로 영향을 주어서는 안된다 <br>
그러기 위해서 tearDown 기능이 있다 <br>
```java
@AfterEach
void tearDown() {
  productRepository.deleteAllInBatch();
}
```

@AfterEach 는 테스트 1개가 끝난 후 메소드를 실행시켜 객체를 초기화한다거나 데이터를 지운다거나 하는 작업을 수행한다 <br>

반대로 @BeforeEach 는 테스트가 메소드 1개 마다 시작전에 사전작업을 진행 하는 것이다 <br>
@AfterEach 는 테스트 메소드 1개가 끝날 떄 마다 실행을 한다. <br>

비슷한 어노테이션으로 @AfterAll, @BeforeAll 이 있고 이 어노테이션은 테스트 시작전 1번, 테스트 끝난 후 1번만 딱 실행이 된다 <br>


### @DataJpaTest 와 @SpringBootTest 차이
@DataJpaTest 는 내부 구현에 @Transactional 이 걸려있다 <br>
즉 테스트에서 @Transactional 이 걸려 있으면 테스트가 끝나면 자동으로 롤백이 된다 <br>

@SpringBootTest 는 @Transactional 이 걸려있지 않아, 테스트 가 끝날 떄 마다 TearDown 메소드를 통해서 데이터를 초기화 해줘야 한다 <br>
TearDown 메소드를 만들기 귀찮아서 테스트에 @Transactional 을 붙일수 있기는 하다 <br>
하지만 테스트 클래스에 @Transactional 을 걸었을 때 큰 문제점이 있다. <br>

@Transactional 어노테이션을 사용하면 @AfterEach 같은 코드를 고민할 필요가 없다 <br> 
삭제 후처리에 대한 고민하지 않아도 되므로 간편하다 <br>

보통 ServiceTest 에서 @Transactional 을 걸어서 롤백을 하거나, 수동 삭제를 통해 객체를 비워버릴 수 있다.
```java

@Transactional
@SpringBootTest
class  OrderServiceTest {
	
}

@SpringBootTest
class OrderServiceTestEx {
	
  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    stockRepository.deleteAllInBatch();
  }
}

```

위 코드중 둘 중 하나를 사용하면 된다 <br>

그리고 @Transactional 이 프로덕션 코드쪽 JpaRepository<> 를 상속받는 레포지토리가 있다면 상위 계층에 가보면 SimpleJpaRepository 구현체를 보면 <br>
이미 메소드에 @Transactional 어노테이션이 걸려있다. 그래서 자동으로 롤백이 되긴한다 <br>


### 새로운 요구사항
- 주문 생성 시 재고 확인 및 개수 차감 후 생성하기
- 재고는 상품번호를 가진다.
- 재고와 관련 있는 상품 타입은 병 음료, 베이커리 이다


### 키워드
추후 재고 관리 프로그램 같은 경우는 '동시성' 에 대한 문제를 생각해볼 수 있다 <br>
보통 위 문제를 해결하기 위해 낙관적 락, 비관적 락 개념을 사용하여 데이터에 대한 락을 잡고 순차적으로 처리하게 끔 한다 <br>
나중에 공부를 좀 더 해보자