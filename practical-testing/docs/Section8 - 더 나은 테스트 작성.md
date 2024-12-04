# 더 나은 테스트를 작성하기 위한 구체적 조언
### 1) 한 문단에 한 주제 
```java
    // bad practice
	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	void containsStockType() {
	    // given
		ProductType[] productTypes = ProductType.values();

		// when & then
		for(ProductType productType : productTypes) {
			if(productType == ProductType.HANDMADE) {
				boolean result = ProductType.containsStockType(productType);

				Assertions.assertThat(result).isFalse();
			}

			if(productType == ProductType.BAKERY || productType == ProductType.BOTTLE) {
				boolean result = ProductType.containsStockType(productType);

				Assertions.assertThat(result).isTrue();
			}
		}
	}
	
	// best practice
	@Test
	@DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
	void containsStockType() {
		// given
		ProductType givenType = ProductType.HANDMADE;

		// when
		boolean result = ProductType.containsStockType(givenType);

		// then
		Assertions.assertThat(result).isFalse();
	}
```

논리구조와, 분기 구조가 테스트 코드에 존재하는 것은 지양하는게 좋다 <br>
테스트코드를 읽는 사람이 한번 더 생각을 하고 코드를 작성해야 하기 때문에 위 코드는 지양하자<br>

추가적으로 여러 케이스에 대한 검증이 필요할 때는 한개의 테스트 코드에 모든 검증을 하지말고 테스트 코드를 따로따로 작성하는 것이 좋다 <br>
ex) 게시판 조건 검색

### 2) 완벽하게 제어하기
> LocalDateTime.now() 

같은 수행하는 환경에 따라 달라지는 것들은 고정된 값(날짜,시간) 을 기준을 정해야 한다 

### 3) 테스트 환경의 독립성을 보장하자
- 한가지 테스트 시 다른 메소드들에 최대한 독립적이어야 한다.
  - 즉 한 메소드를 테스트 할 때 그 메소드를 테스트 하기 위한 다른 메소드들에게 간섭을 받아서는 안된다.

### 4) 테스트 간 독립성을 보장하자
- 두개 이상의 테스트 간 독립성이 보장되어야 한다.

```java
private static final Stock stock = Stock.create("001",1);
```

전역변수(=공유자원) 처럼 모두가 사용하는 것에 대해서는 독립성이 필요하다. 1개의 테스트에는 통과할 수 있어도 다른 테스트 에서는 실패할 수 있다 <br> 


### 5) 한 눈에 들어오는 Test Fixture 구성하기
> @BeforeEach, @AfterEach, @BeforeAll, @AfterAll 
- Fixture : 고정물, 고정되어 있는 물체
  - 대부분 given 절에 생성하는 모든 객체들의 의미한다.
- 테스트를 위해 원하는 상태로 고정시킨 일련의 객체

테스트 환경을 위해서 원하는 상태값으로 구성시킨 객체들의 의미한다 <br>
같은 given 데이터를 만드는 경우가 정말 많다. <br>
위 같은 결합도를 줄이기 위한 작업이 필요할 것이다. <br>

테스트간 결합도를 줄이기 위한 작업이 필요하다 <br>
- 즉 각 테스트 입장에서 봤을 떄 : 아예 몰라도 테스트 내용을 이해하는 데 문제가 없는가 ? <br>
- 수정해도 모든 테스트에 영향을 주지 않는가? 

위 두 내용을 만족하면 @BeforeEach 어노테이션에 들어갈 명분이 있다
```java
@BeforeEach
void setUp() {
	
}
```

- 테스트 전체에서 사용하는 추상 클래스를 만들어서 @Builder 를 모아 둘 수도있다? -> 크게 추천하지는 않는다  <br>
  - 코틀린을 사용한다면 위 문제는 바로 해결이 된다.. 코틀린을 배워야 하나..
    - 롬복, 빌더 등 사용할 필요없고, 생성자로 모든 문제를 해결할 수 있다.

### 6) Test Fixture 클렌징
> deleteAll,  deleteAllInBatch 차이를 알아보자.
> > @Transactional 을 사용하여 롤백 클렌징을 통하여 사용할 수 있다 -> 사이드 이펙트를 잘 알고 사용해야 한다.

- ✅deleteAll
```java
	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAll();
		productRepository.deleteAll();
		orderRepository.deleteAll();
	}
```

-> deleteAll 은 'delete from orderProduct where id = ?' 쿼리를 실행한다 <br>
deleteAll 은 전체 테이블을 조회하는 select 를 날리고 데이터를 건당 삭제를 한다. 즉 데이터를 건별로 지운다 <br>
데이터가 3개면 select 쿼리를 날린 후 delete 쿼리가 3개가 나온다. <br>

- ✅deleteAllInBatch
```java
	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
	}
```

-> deleteAllInBatch 는  'delete from orderProduct' 쿼리가 아래 차례대로 날아간다. <br>

위 2개를 사용할 떄의 성능적인 차이가 분명히 있을 수 있다 <br>

📌 위 2개를 사용하기 싫다면 @Transactional 을 걸어서 사용해도 좋다 <br>


### 7) @ParameterizedTest






### 8) @DynamicTest






### 9) 테스트 수행도 비용이다. 환경 통합하기






### 10) private 메소드 테스트는 어떻게 할까






### 11) 테스트에서 필요한 메소드가 있는대, 프로덕션 코드에는 필요가 없다면?






### 12) 키워드 정리