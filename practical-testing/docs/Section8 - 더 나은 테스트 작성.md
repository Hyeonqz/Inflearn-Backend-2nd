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


### 3) 테스트 환경의 독립성을 보장하자

### 4) 테스트 간 독립성을 보장하자

### 5) 한 눈에 들어오는 Test Fixture 구성하기

### 6) Test Fixture 클렌징

### 7) @ParameterizedTest

### 8) @DynamicTest

### 9) 테스트 수행도 비용이다. 환경 통합하기

### 10) private 메소드 테스트는 어떻게 할까

### 11) 테스트에서 필요한 메소드가 있는대, 프로덕션 코드에는 필요가 없다면?

### 12) 키워드 정리