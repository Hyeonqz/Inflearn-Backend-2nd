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














































