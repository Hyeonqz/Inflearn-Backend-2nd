# Section3  - 단위 테스트
## 수동 테스트 vs 자동화된 테스트
[수동 테스트]
```java
	@Test
	@DisplayName("더하기 테스트")
	void add() {
	    // given
		CafeKiosk cafeKiosk = new CafeKiosk();

		// when
		cafeKiosk.add(new Americano());

		// then
		System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBeverages().size());
		System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBeverages().getLast().getName());
	}
```

이 코드가 과연 자동화를 위한 테스트 일까?? 라는 고민을 해봐야 합니다 <br>
위 코드의 최종적인 확인 주체는 사람입니다. <br>
위 문제는 사람이 개입을 해야하고, 다른 사람이 테스트 코드를 봤을 때, 뭘 검증해야하고 뭐가 맞고 틀린지를 알 수가없다 <br>

### Junit5 를 통한 자동화된 테스트 하기
#### 단위 테스트란?
- 작은 코드 단위를 독립적으로 검증하는 테스트
  - 작은 코드 단위 : 클래스 or 메소드
- 검증 속도가 빠르고, 안정적이다.

#### Junit5 
- 단위 테스트를 위한 테스트 프레임워크
  - XUnit - Kent Beck 저자가 여러 단위 테스트 프레임워크를 만듬.
  - https://junit.org/junit5/docs/current/user-guide/

#### AssertJ
- Junit 에 얹어서 사용하는 편이다.
- 테스트 코드 작성을 원활하게 도와주는 테스트 라이브러리
- 풍부한 API, 메소드 체이닝 지원
  - https://assertj.github.io/doc/

[자동화 테스트]
```java
	@Test
	@DisplayName("자동화 더하기 테스트")
	void automatic_Add() {
		CafeKiosk cafeKiosk = new CafeKiosk();

		cafeKiosk.add(new Americano());

		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);
		assertThat(cafeKiosk.getBeverages().getFirst().getName()).isEqualTo("아메리카노");
	}

	@Test
	@DisplayName("삭제 테스트")
	void remove() {
	    // given
		CafeKiosk cafeKiosk = new CafeKiosk();
		Beverage beverage = new Latte();

		cafeKiosk.add(beverage);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);

		cafeKiosk.delete(beverage);
		assertThat(cafeKiosk.getBeverages().isEmpty());
	}
```

위 코드는 console 에 값이 뜨는게 아닌, 테스트 케이스가 통과하나 못하나로 success,fail 이 나뉜다 <br>

## 테스트 케이스 세분화 하기
### 요구사항 추가
- 한 종류의 음료 여러 잔을 한번에 담는 기능

질문하기: 암묵적이거나 아직 드러나지 않은 요구사항이 있는가? <br>

- 해피 케이스 : 요구사항을 만족하는 테스트
- 예외 케이스 : (암묵적이였음) 요구사항 외에 예외적인 테스트

위 둘 케이스를 테스트 하기 위해선 **'경계값 테스트'** 를 잘해야 한다 <br>

## 테스트 하기 어려운 영역을 분리하기
경계값 테스트를 사용하여 예외상황에 대해서도 테스트를 해보자
```java
  @Test
  @DisplayName("영업시간이 아닐 때 예외 발생 테스트")
  void createOrderOutsideOpenTime() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);

    assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 10, 16, 22, 1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요")
    ;

  }
```

### 테스트 하기 어려운 영역을 구분하고 분리하기
우리가 검증하고자 하는 것은 메소드 그 자체이다 <br>
어떠한 테스트 값이 주어졌을 때 그 메소드가 잘 통과하는지가 중요하다 <br>

- 메소드 내부 값들을 외부(=파라미터) 로 분리할수록 테스트 가능한 코드는 많아진다 

#### 그럼 테스트하기 어려운 영역은?
- 관측할 때마다 다른 값에 의존하는 코드
  - 현재 날짜/시간, 랜덤 값, 전역 변수/함수, 사용자 입력 값.
- 외부 세계에 영향을 주는 코드
  - 표준 출력, 메시지 발송, 데이터베이스에 기록하기 등

#### 테스트하기 쉬운 영역은? -> 순수 함수
- 같은 입력에는 항상 같은 결과
- 외부 세상과 단절된 형태
- 테스트하기 쉬운 코드


이러한 시야를 기르는 능력이 중요하다 <br>
테스트 코드를 많이 짜봐야지 이런 경험이 생길 것이다. 테스트를 짜기 위해 노력을 해봐라 <br>

### 키워드 정리
- 단위 테스트
- 수동 테스트 vs 자동 테스트
- Junit5, AssertJ
- 해피케이스, 예외 케이스
- 경계값 테스트
- 테스트 하기 쉬운/어려운 영역(순수함수)

#### 개인적으로 공부할 키워드
- lombok (남발시 유지보수하기 어려울 때가 많다)
  - @Data, @Setter 를 지양하자.
  - JPA 에서 양방향 연관관계 시 @ToString 순환 참조 문제 -> 해결하기 위해선 DTO 를 사용하자.