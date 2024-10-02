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






























