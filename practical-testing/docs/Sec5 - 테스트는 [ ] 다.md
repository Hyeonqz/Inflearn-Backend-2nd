# Section - 테스트는 [ ] 다.
#### 📌 테스트는 [문서]다

### 문서?
- 프로덕션 기능을 설명하는 테스트 코드 문서
- 다양한 테스트 케이스를 통해 프로덕션 코드를 이해하는 시각과 관점을 보완
- 어느 한 사람이 과거에 경험했던 고민의 결과물을 팀 차원으로 승격시켜서, 모두의 자산으로 공유할 수 있다.

#### 우리는 항상 팀으로 일한다
- 내가 작성한 코드 및 문서가 다른 팀원에게 어떻게 비칠지에 대한 고민을 하면서 작성을 해야 한다.
- 더 좋은 테스트 코드를 어떻게 작성해야 할지에 대한 이야기를 나눠보자.

## DisplayName 섬세하게
Junit5 에서 @DisplayName 이라는 어노테이션이 생겼다. <br>
String 값을 넣어서 아래 테스트가 무슨 역할을 하는지 설명을 해줄 수 있다 <br>
[이전방식의 테스트]
```java
@Test
void 음료_추가_테스트() {
	// 테스트 로직
}
```

[@DisplayName 을 사용한 테스트]
```java
@DisplayName("음료를 1개 추가하면 주문 목록에 담긴다.")
@Test
void noAutomatic_add () {
    // 테스트 로직        
}
```

> 명사의 나열보다 문장으로 'A이면 B이다' 이런식으로 작성을 해야 명확하고 좋다<br>
>> ~테스트 로 끝나는 것은 지양한다.   

- 도메인 용어를 사용하여 한층 추상화된 내용을 담도록 노력을 하자
  - 메소드 자체의 관점보다 도메인 정책 관점으로 내용을 담자
- 테스트의 현상을 중점으로 기술하지 말것
  - ex) ~성공한다, 실패한다 등의 내용을 기술하지 말자. 

## BDD 스타일로 작성하기
BDD : Behavior Driven Development <br>

- TDD 에서의 파생된 개발 방법
- 함수 단위의 테스트에 집중하기 보다, 시나리오에 기반한 테스트 케이스 자체에 집중하여 테스트 한다.
- 개발자가 아닌 사람이 봐도 이해할 수 있을 정도의 추상화 수준(레벨)을 권장.

> Given / When / Then 

- Given: 시나리오 진행에 필요한 모든 준비 과정(객체, 값, 상태)
- When: 시나리오 행동 진행
- Then: 시나리오 진행에 대한 결과 명시, 검증

즉 어떤 환경에서, 어떤 행동을 진행했을 때, 어떤 상태 변화가 일어난다 <br>
-> DisplayName 에 명확하게 작성할 수 있다. 

```java
	@Test
	@DisplayName("아메리카노를 키오스크에 더한다.")
	void automatic_Add () {
	    // given
		CafeKiosk cafeKiosk = new CafeKiosk();

		// when
		cafeKiosk.add(new Americano());

		// then
		assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
		assertThat(cafeKiosk.getBeverages()).hasSize(1);
		assertThat(cafeKiosk.getBeverages().getFirst().getName()).isEqualTo("아메리카노");
	}
```

### 키워드 정리
- @DisplayName - 도메인 정책, 용어를 사용한 명확한 문장
- Given/When/Then - 주어진 환경, 행동, 상태 변화
- TDD vs BDD

#### 알아볼 키워드
- Junit vs Spock


#### 언어가 사고를 제한한다.
우리가 한번 규정해둔 것에 갇히면 우리의 사고도 제한이 된다