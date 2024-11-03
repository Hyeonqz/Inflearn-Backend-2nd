# Day15 미션
> Layer Architecture 구조의 레이어별 특징 및 어떻게 테스트를 하면 좋을지에 관한 생각을 적어보자

https://sungbin.kr/%EC%9D%B8%ED%94%84%EB%9F%B0-%EC%9B%8C%EB%B0%8D%EC%97%85-%EC%8A%A4%ED%84%B0%EB%94%94-%ED%81%B4%EB%9F%BD-2%EA%B8%B0-%EB%B0%B1%EC%97%94%EB%93%9C(%ED%81%B4%EB%A6%B0%EC%BD%94%EB%93%9C-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%BD%94%EB%93%9C)-Day15-%EB%AF%B8%EC%85%98/

### Persistence Layer
SpringBoot&JPA 를 사용한다는 가정하에서 Persistence Layer 는 JpaRepository 를 상속받는 엔티티에 영향을 주는 것들을 테스트 하는 계층이라고 생각한다 <br>

이미 JpaRepository 에 구현되어 있는 기능들을 제외하고 일반적으로는 상황에 따라 데이터를 어떻게 조회할지에 대한 고민을 많이하게 될 것이고 <br>
조회가 많을 수록 이미 추상화가 잘되어있는 스프링 메소드들을 쓰는게 아닌 내가 JPA 포맷에 맞춰서 메소드 네이밍을 할 것이다 <br>

그리고 내가 방금 만든 Spring Data JPA 메소드 들을 테스트 하는 Layer 가 바로 Persistence Layer 라고 생각한다 <br>

요약하자면 데이터를 주고 받는 서빙 영역을 테스트 하는 것이다 <br>
데이터를 주는 쪽이 있으면 받는 쪽도 존재하므로, 위 상호 관계를 테스트 하는것 이다

위 Layer 를 테스트 하는 방법으로는 2가지가 있다.
- Mock 을 사용하여 DB 에 의존하지 않는 단위 테스트
  - @MockBean 사용

[1) MockBean 사용 예시]
```java
class TestExample {
	@MockBean
    private OrderRepository orderRepository;
	
	@Test
    void testFindOrderById() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(new Order("피자")));
		Order order = orderServcie.findOrderById(1L);
		assertEquals("피자", order.getOrderName());
    }
}
```

- @DataJpaTest 를 사용한 통합 테스트
  - H2 DB 를 사용하는 것을 권장
  - 상황에 따라 MySQL, PostegreSQL 등 잘 판단하여 테스트를 하면 된다.

[2) @DataJpaTest 예시]
```java
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
	
    @Test
    void testSaveOrder() {
        Order user = new User("피자");
		orderRepository.save(user);

		assertThat(user.getId()).isNotNull();
    }
}
```

추가적으로 @DataJpaTest 는 Jpa 관련 클래스 들을 Bean 정보를 가져오지만 <br>
@SpringBootTest 를 사용해야지 src/main 아래에 있는 Bean 객체들을 다 가져올 수 있다 <br>

@Bean Qualifier 문제가 생긴다면 위 어노테이션을 한번 체크해볼 필요가 있다

### Service Layer
실제 비즈니스 로직을 테스트 하는 Layer 이다 <br>
실제로 Service 계층에는 복잡한 비즈니스 코드들이 존재할 것이다 <br>
그리고 그에 따라 테스트를 짜기 복잡할 수도 있다. (내가 그렇다...) <br>

그리고 위 Layer 를 테스트 할 때는 @Transactional 을 사용해 트랜잭션을 적용한다 <br>
중간에 실패할 수 있는 트랜잭션이 있을 때 유용하게 써먹을 수 있다 <br>

위 Layer 도 테스트 하는 방식은 위 Persistence Layer 와 비슷하다. <br>
하지만 비즈니스가 복잡하면 복잡할수록 위 Layer 테스트 코드 또한 복잡해질 것이다.. <br>

[1) @Mock 을 사용한 단위 테스트 - Junit5 ]
```java
@MockBean
private UserRepository userRepository;

@Test
void testCalculateDiscount() {
    User user = new User("quaka", 3, 3000); 
	
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
	
    double total = discountService.calculateTotal(1L);
    assertEquals(9000, total);
}
```

[2) @SpringBootTest 를 사용한 통합 테스트 - Junit5]
```java
@SpringBootTest
class orderServiceTest {
	
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
	
    @Test
    @Transactional
    void testOrderProcessing() {
        Product product = new Product("트랙패드", 3); // 재고3
        productRepository.save(product);
		
        orderService.orderProduct(product.getId(), 2); // 2개 주문
        Product updatedProduct = productRepository.findById(product.getId()).get();
        assertEquals(2, updatedProduct.getStock()); // 재고 1개 남음
    }
	
}
```

@Transactional 이 붙으면 위 메소드가 실행 후 롤백이 된다 <br>

### Presentation Layer
사용자의 요청을 받아 비즈니스 레이어로 전달하고 그 결과를 사용자에게 반환하는 역할을 함 <br>
주로 컨트롤러에서 사용자의 요청을 처리하고 적절한 응답을 제공하며 이 레이어는 웹 브라우저나 API 요청과 직접적으로 상호작용 하고, JSON, HTML, XML과 같은 포맷으로 데이터를 응답한다. <br>

보통 프레젠테이션 레이어는 컨트롤러 테스트를 통해 HTTP 요청과 응답이 정상적으로 처리되는지 확인한다. 또한 파리미터에 대한 최소한의 검증로직이 포함된다.

[1) MockMvc 를 사용한 단위 테스트] -> 외부 의존성 또한 모두 Mocking 한다.
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
	
    @Test
    void getUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(new User("JIN HYEON KYU"));
        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("JIN HYEON KYU"));
    }
}
```

[2) 통합테스트]
- 컨트롤러, 비즈니스, 퍼시스턴스 총 3개의 레이어가 다 상호작용하는지를 테스트 한다.
- 이 테스트에서는 전체 어플리케이션 컨텍스트를 로드한다 -> Bean 을 다 부른다
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
	
    @Test
    void userCreate() throws Exception {
        //given
        UserCreateRequest request = UserCreateRequest.builder()
                .name("JIN HYEON KYU")
                .build();
		
        // when / then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("JIN HYEON KYU"));
    }
}
```

나도 나중에 기억이 안날 때 내가 작성한 위 미션을 보고 리마인드를 해야겠다.