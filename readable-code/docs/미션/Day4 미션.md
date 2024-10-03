# Day4 미션

### 1. 아래 코드와 설명을 보고, [섹션 3. 논리, 사고의 흐름]에서 이야기하는 내용을 중심으로 읽기 좋은 코드로 리팩토링해 봅시다.
```java
public boolean validateOrder(Order order) {
    if (order.getItems().size() == 0) {
        log.info("주문 항목이 없습니다.");
        return false;
    } else {
        if (order.getTotalPrice() > 0) {
            if (!order.hasCustomerInfo()) {
                log.info("사용자 정보가 없습니다.");
                return false;
            } else {
                return true;
            }
        } else if (!(order.getTotalPrice() > 0)) {
            log.info("올바르지 않은 총 가격입니다.");
            return false;
        }
    }
    return true;
}
```

- 사용자가 생성한 '주문'이 유효한지를 검증하는 메서드.
- Order는 주문 객체이고, 필요하다면 Order에 추가적인 메서드를 만들어도 된다. (Order 내부의 구현을 구체적으로 할 필요는 없다
- 필요하다면 메서드를 추출할 수 있다.

<br>
위 코드를 Sec3 - '논리,사고의 흐름' 에서 배운 내용을 토대로 리팩토링을 해보겠습니다.

```java
public boolean validateOrder(Order order) {
    if (isOrderItemsZero(order)) 
        return false;
    
    if (isTotalPriceValid(order)) 
        return hasNoCustomerInfo(order); // 고객 정보가 없으면 true, 있으면 false
    else 
        return false;
}

private static boolean isOrderItemsZero(Order order) {
    if (order.getItems().size() == 0) {
        log.info("주문 항목이 없습니다.");
        return true;
    }
    return false;
}

private static boolean isTotalPriceValid(Order order) {
	if (order.getTotalPrice() <= 0) {
		log.info("올바르지 않은 총 가격입니다.");
		return true;
	}
	return false;
}

private static boolean hasNoCustomerInfo(Order order) {
    if (order.getInfo().size() == 0) {
        log.info("사용자 정보가 없습니다.");
        return true;
    }
    return false;
}

```
#### 1. early return
모든 메소드에 early return 을 함으로써 불필요 로직이 실행되지 않게 하였음.

#### 2. 메소드 추출
```java
private static boolean isOrderItemsZero(Order order) {
	if (order.getItems().size() == 0) {
		log.info("주문 항목이 없습니다.");
		return true;
	}
	return false;
}

private static boolean isTotalPriceValid(Order order) {
	if (order.getTotalPrice() <= 0) {
		log.info("올바르지 않은 총 가격입니다.");
		return false;
	}
	return true;
}

private static boolean hasNoCustomerInfo(Order order) {
	if (order.getInfo().size() == 0) {
		log.info("사용자 정보가 없습니다.");
		return true;
	}
	return false;
}

```

메소드 추출을 통해 메소드에 구현부를 줄임으로서 가독성이 향상 되었습니다 <br>

#### 3. 부정어 처리
```java
private static boolean hasNoCustomerInfo(Order order) {
    if (order.getInfo().size() == 0) {
        log.info("사용자 정보가 없습니다.");
        return true;
    }
    return false;
}
```

메소드 추출을 하였고, ! 부정어를 줄이기 위해 Negative -> Positive 한 로직으로 바꾸었습니다 <br>
<br>
위 과정들을 통하여 가독성 향상 및 유지보수 하기 쉽게 리팩토링을 해보았습니다

### 2. SOLID에 대하여 자기만의 언어로 정리해 봅시다.
SOLID 원칙을 실제로 제가 즐겨하는 축구 경기에 비유를 해보겠습니다

#### SRP
즉, 하나의 객체는 하나의 책임(=역할)을 가져야 하는 원칙이다 <br>
축구에서는 선수 마다 개인의 능력에 맞는 '**포지션**'이 존재한다 <br>

그리고 선수들은 포지션에 맞는 역할만 잘 하면 된다 <br>
ex) 골키퍼는 골을 막아야하고, 공격수는 골을 넣어야 하며, 수비수는 골을 막기위해 노력해야 하고, 심판은 경기를 중재하면 되는 것이다 <br>

예외적인 상황으로, 공격수가 수비도 하고, 수비수가 골도 넣긴 하지만, 프로그래밍 관점에서 보면 위 활동은 결합도가 높아진 다는 것을 의미할 수도 있다 <br>

#### OCP
open 과 close 가 확실히 되어야 함을 의미한다 <br>

선수에 맡는 Role(=역할,책임) 이 있기에 , 감독이 경기 상대에 따라 전술을 유연하게 바꾸는 것 과 비슷하다 <br>
기존 선수들의 Role 을 깨지 않고, 유동적으로 전략을 바꾸는 것을 의미한다 <br>

#### LSP
서브클래스는 항상 부모 클래스를 대신할 수 있어야 한다 <br>
그 뛰어난 축구선수 호날두,메시가 있지만, 호날두 메시 에 포지션을 대체할 수 있는 선수는 많다 <br>

다른 예시를 들면, 주전 선수와 후보 선수를 의미한다 <br>
주전선수가 힘들면, 후보선수 중에 동일한 역할을 하는 선수로 교체시키는 것 과 같다 <br> 

#### ISP
인터페이스 분리 원칙은, 결합도를 낮추는 방법으로 다른 메소드에 의존하지 않도록 설계하는 원칙이다 <br>
약간 SRP 원칙이랑 느낌이 비슷하다고 생각한다 <br>

즉, 자신이 맡은 역할만을 잘 해내는 것을 의미한다. <br> 
각 포지션에 맞는 역할과 책임이 있는데 위 책임을 다같이 나눈다? 뭔가 이상하다 <br>

예시를 보자
```java
interface SoccerPlayer {
    void attack();  
    void defend();  
    void saveGoal();  
}

```

한명의 축구 선수 역할을 정의하기 위해 만들어둔 인터페이스라고 이다 <br>
그 한명의 축구 선수는 손흥민이다 <br>
손흥민은 공격수인데 저 4가지 메소드를 과연 다 사용할까? <br>

아니다 공격수는 attack(), 수비수는 defend(), 골키퍼는 saveGoal(); 각자 역할에 맞는 메소드만 수행을 하면된다 <br>

올바른 코드를 보자 
```java
interface Attacker {
    void attack(); 
}

interface Defender {
    void defend(); 
}

interface Goalkeeper {
    void saveGoal();  
}

```

위 처럼 역할에 맞게 인터페이스를 분리해서, 사용하지 않는 메소드들은 역할에 맞게 분리를 해야한다, 즉 꼭 필요한 것만 기능을 만들어서 사용을 하자 <br>


#### DIP
DIP는 상위 모듈이 하위 모듈에 의존하지 않고, 둘 다 추상화된 인터페이스에 의존해야 한다는 원칙이다. <br>
축구에서 감독이 지시를 하므로 최상위 모듈이고, 선수들은 그 아래 하위 모듈이다 <br>

감독이 특정 선수에게 구체적인 동작을 지시하여 직접 의존한다면? 만약 지시를 하지 않은 다른 선수가 출전한다면? <br> 
그럼 감독은 그 다른 선수에게 또 다시 지시를 해야 합니다 <br> 

그러므로 감독은 각 선수들 에게 지시가 아닌, **추상적인 역할(인터페이스)**에 지시를 해야 한다.  <br>
이렇게 하면, 감독은 각 선수들 에게 매번 지시를 할 필요가 없고, 선수가 교체되어도 지시해둔 명령에 따르기만 하면 된다 <br>

DIP에서는 **고수준 모듈(감독)**이 **저수준 모듈(선수들의 구체적인 구현)**에 직접 의존하지 않고, **추상적인 인터페이스(역할)**에 의존해야 합니다. 이를 통해 시스템의 유연성과 확장성을 보장할 수 있습니다.
위 DIP 를 지키지 않는다면, 매번 코드를 바꿔야 하는 불상사를 겪을 수 있다.<br>

객체지향 5가지 원칙인 SOLID 를 다 지키면서 프로그래밍을 하는 것은 제가 생각하기에 어렵다고 생각이 듭니다 <br>
하지만 위 원칙을 잘 인지하고, 적용하기 위해 최대한 노력을 한다면 어플리케이션의 유지보수, 확장성, 유연성이 훨씬 좋아질 것이라고 예상한다 <br>
추가적으로 본인의 프로그래밍 능력과 사고하는 능력또한 많이 향상 될 것이라고 생각한다 <br>