# TDD: Test Driven 
현재 까지는 프로덕션 코드를 작성하고 테스트를 작성했다 <br>
TDD 는 프로덕션 코드보다 테스트 코드를 먼저 작성하여 테스트가 구현 과정을 주도하도록 하는 개발 방법론 이다 <br>

RED - GREEN - REFACTOR 위 사이클을 가진다 
- RED: 일부로 실패하는 테스트를 작성한다.
- GREEN: 테스트 통과 최소한의 코딩 -> 막 작성해도 됨(테스트가 통과할 최소한의 코딩을 한다)
- REFACTOR: 구현 코드 개선, 테스트 통과 유지 시킨다.


#### 1단계 - RED
```java
  @Test
  @DisplayName("TDD 1단계")
  void calculatorTotalPrice() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    Latte latte = new Latte();

    cafeKiosk.add(americano);
    cafeKiosk.add(latte);

    int totalPrice = cafeKiosk.caculatorTotalPrice();
    assertThat(totalPrice).isEqualTo(8500);
  }

public int caculatorTotalPrice() {
  return 0;
}
```

최소한의 테스트가 동작할 수 있도록 한다. 위 테스트는 실패한다. <br>

#### 2단계 - GREEN
이후에 테스트가 통과는 할 수 있게 메소드를 수정한다
```java
public int caculatorTotalPrice() {
  return 0;
}
```

#### 3단계 - REFACTOR
```java
	public int calculateTotalPrice () {
		return beverages.stream()
			.mapToInt(Beverage::getPrice)
			.sum();
	}
```

기능을 리팩토링한 후 테스트를 돌리면 테스트가 통과하는 것을 확인할 수 있다

#### TDD 핵심 가치 -> 피드백
- 테스트 자체의 누락 가능성
- 특정 테스트 케이스(=해피 케이스) 만 검증할 가능성
- 잘못된 구현을 다소 늦게 발견할 가능성

TDD 를 하는 이유 중 하나는 일반 적인 기능 구현 후 테스트는 해피케이스만 생각하게 되어 예외 케이스에 대하여 방어를 하지 않는 경우가 많다 <br>

TDD 를 하면 예외 케이스에 대한 누락을 방지할 수 있다 <br>
테스트를 먼저 작성하면, 테스트를 통과하기 위한 구조를 생각하게 된다 <br>

#### 선 테스트 작성, 후 기능 구현 장점
- 복잡도가 낮은, 테스트 가능한 코드로 구현할 수 있게 된다.
- 쉽게 발견하기 어려운 엣지 케이스를 놓치지 않게 해준다.
- 구현에 대한 피드백을 받을 수 있다.
- 과감한 리팩토링이 가능해진다.

#### TDD: 관점의 변화
- 테스트는 구현부 검증을 위한 보조 수단.

하지만 TDD 는 테스트와 프로덕션 코드가 상호작용하여 발전하는 구현부 이다.<br>
클라이언트(=객체) 관점에서의 피드백을 주는 TestDriven


### 키워드 정리
- TDD
- 레드-그린-리팩토링

### 알아볼 키워드
- 애자일 방법론 vs 폭포수 방법론
- 애자일 방법론
  - 익스트림 프로그래밍(XP) -> TDD 는 XP 에서부터 시작이 됨
  - 스크럼, 칸반