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

## Mock 을 사용한 Controller 테스트
[1번 방법]
```java
@AutoConfigureMockMvc
@SpringBootTest
```

[2번 방법]
```java
@WebMvcTest(controllers = xxxController.class) // 사용할 컨트롤러
```

프론트랑 백엔드가 나누어져있을 때 응답 이 규격화된 골격이 있으면 서로 편하다 <br>
```java
public class ApiResponse<T> {

	private HttpStatus status;
	private int code;
	private String message;
	private T data;

	public ApiResponse (HttpStatus status, String message, T data) {
		this.code = status.value();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse <T> of (HttpStatus status, T data) {
		return new ApiResponse<>(status,status.name(), data);
	}

	public static <T> ApiResponse <T> ok (T data) {
		return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);
	}

}

```

#### [@Valid 어노테이션 검증 테스트]
컨트롤러에서 단위테스트에 가까운 프론트에서 요청한 값이 Valid 한지 알아보는 테스트를 해보려고 한다 <br>
```java
@Builder
public record ProductCreateRequest(
        String productNumber,

        @NotNull(message = "상품 타입은 필수입니다.") // client 에 보낼 메시지를 적어준다.
        ProductType type,

        @NotNull(message = "상품 판매상태는 필수입니다.") // enum 같은 객체 또는 type 일 경우
        ProductSellingType sellingType,

        @NotBlank(message = "상품 이름은 필수입니다.") // String 일 경우
        String name,

        @Positive(message = "상품 가격은 양수여야 합니다.") // 숫자 일 경우
        int price
) {

}
```
```java
@WebMvcTest(controllers = ProductController.class) // Controller 관련 Bean 들만 컨텍스트를 띄우는 것
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	/*
	 * mockito 라이브러리의 어노테이션
	 * 컨테이너에 Mockito 로 만든 Bean 을 넣어주는 역할을 한다.
	 * */
	@MockBean
	private ProductService productService;

	@Test
	@DisplayName("신규 상품 등록시 상품 타입은 필수값이다.")
	void createProductWithoutType () throws Exception {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.sellingType(ProductSellingType.SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		// when & then
		// http body 에 값을 넣다보면 직렬화, 역직렬화 과정을 거치게 된다.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new") // perform -> api 수행
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print()) // 요청 log 출력
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 타입은 필수입니다."))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
		;
	}

}
```
위 Spring Validation 어노테이션을 검증하는 테스트 코드를 위처럼 작성할 수 있다 <br>

#### 추가적인 검증
String 을 검증 할 때 Spring Validation 에서 3가지 어노테이션이 헷갈린다.
> @NotBlank -> 아래 값들 모두가 통과하지 못한다, 
> > @NotNull -> "" " " 이런 것들은 통과 , @NotEmpty -> "     " 은 통과한다.

@Validation 에 대한 책임 분리 ? <br>
ex) 상품이름은 최대 글자 수는 20자 제한이 있다? <br>
@Max(20) 으로 제한을 하는 방법도 있다.


#### [파라미터가 없을 때 컨트롤러 단위 테스트]
```java
	@Test
	@DisplayName("판매 상품을 조회한다")
	void getSellingProducts() throws Exception {
		// given
		List<ProductResponse> result = List.of();
		Mockito.when(productService.getSellingProducts()).thenReturn(result);

		// when & then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling")
		)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
		;

	}
```

Get 요청에서는 ObjectMapper 를 주입받을 필요가 없다 <br> 
직렬화 & 역직렬화가 안 일어나기 때문이다 <br>

@Validation 만 하는 DTO 를 만들고 그 DTO 를 보통 사용하는 Request DTO 로 만드는 방법 또한 좋다 