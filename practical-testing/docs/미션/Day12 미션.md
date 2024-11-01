# Day12 미션
> 미션 내용	"[Readable Code] 강의의 두 프로젝트(지뢰찾기, 스터디카페) 중 하나를 골라, 단위 테스트를 작성해 봅시다.

- ✔️각 프로젝트 모두 강의 중에 작성한 tobe 패키지 코드를 기준으로 함 (lesson 6-4 가 가장 마지막 버전)
- ✔️3개 이상의 서로 다른 클래스 & 총 7개 이상의 테스트 작성 (시간이 된다면 더 많이 작성해보면 좋겠죠? 😉)
- ➡️ 단, 같은 인터페이스를 구현하고 있는 구현체들은 1개 클래스로 간주한다.
  - (ex. LandMineCell, NumberCell, EmptyCell에 각자 테스트를 작성했어도, 1개 클래스로 간주.)
- ✔️무엇을 테스트하고자 했는지를 잘 나타낸 @DisplayName 작성하기
- ✔️BDD(given/when/then) 스타일 따르기 (주석으로 표기)"

> 비고	"[참고할 만한 조언]

- 💡테스트가 필요하다고 판단하는 과정부터 시작이다.
- 어떤 클래스, 메서드를 테스트하고 싶은지 명확한 이유와 함께 고민해보자. (여긴 이러저러해서 테스트 코드가 꼭 필요하겠군!)
- 💡가장 작은 단위의 메서드부터 테스트를 작성해 보자.
- 처음부터 큰 단위에 대해 테스트를 시도하는 것은 고려할 점이 많아진다."


### StudyCafe 도메인 단위 테스트
[StudyCafePassOrderTest]
```java
class StudyCafePassOrderTest {

    @Test
    @DisplayName("좌석과 락커 이용권이 포함된 주문의 총 가격을 정확하게 계산해야 한다")
    void shouldCorrectlyCalculateTotalPriceWithLocker() {
        // Given: 좌석 이용권과 락커 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 50000, 0.1);
        StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(StudyCafePassType.FIXED, 30, 10000);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, lockerPass);

        // When: 총 가격 계산
        int totalPrice = order.getTotalPrice();

        // Then: 할인 적용 후 정확한 총 가격이어야 함 (50000 + 10000 - 5000)
        assertThat(totalPrice).isEqualTo(55000);
    }

    @Test
    @DisplayName("락커 이용권이 포함되지 않은 주문의 총 가격을 정확하게 계산해야 한다")
    void shouldCorrectlyCalculateTotalPriceWithoutLocker() {
        // Given: 좌석 이용권만 포함된 주문 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, null);

        // When: 총 가격 계산
        int totalPrice = order.getTotalPrice();

        // Then: 할인 적용 후 정확한 총 가격이어야 함 (10000 - 1000)
        assertThat(totalPrice).isEqualTo(9000);
    }

    @Test
    @DisplayName("할인된 좌석 이용권 가격을 정확하게 계산해야 한다")
    void shouldCorrectlyCalculateDiscountPrice() {
        // Given: 할인율이 적용된 좌석 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.WEEKLY, 7, 20000, 0.15);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, null);

        // When: 할인 가격 계산
        int discountPrice = order.getDiscountPrice();

        // Then: 정확한 할인 가격이어야 함 (20000 * 0.15)
        assertThat(discountPrice).isEqualTo(3000);
    }

    @Test
    @DisplayName("락커 이용권이 포함된 경우 Optional로 반환되어야 한다")
    void shouldReturnLockerPassAsOptionalWhenIncluded() {
        // Given: 락커 이용권 포함된 주문 생성
        StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(StudyCafePassType.FIXED, 30, 10000);
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 50000, 0.1);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, lockerPass);

        // When: 락커 이용권 가져오기
        Optional<StudyCafeLockerPass> result = order.getLockerPass();

        // Then: Optional로 락커 이용권이 포함되어 있어야 함
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(lockerPass);
    }

    @Test
    @DisplayName("락커 이용권이 포함되지 않은 경우 Optional.empty()를 반환해야 한다")
    void shouldReturnEmptyOptionalWhenLockerPassNotIncluded() {
        // Given: 락커 이용권 없이 주문 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, null);

        // When: 락커 이용권 가져오기
        Optional<StudyCafeLockerPass> result = order.getLockerPass();

        // Then: Optional.empty()이어야 함
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("주문에서 좌석 이용권이 올바르게 반환되어야 한다")
    void shouldReturnSeatPassCorrectly() {
        // Given: 좌석 이용권 포함된 주문 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 50000, 0.1);
        StudyCafePassOrder order = StudyCafePassOrder.of(seatPass, null);

        // When: 좌석 이용권 가져오기
        StudyCafeSeatPass result = order.getSeatPass();

        // Then: 좌석 이용권이 정확히 반환되어야 함
        assertThat(result).isEqualTo(seatPass);
    }
}
```

[StudyCafeSeatPassTest]
```java
class StudyCafeSeatPassTest {

    @Test
    @DisplayName("고정석 이용권은 사물함을 이용할 수 없다")
    void shouldReturnTrueWhenLockerNotAllowedForSeatPass() {
        // Given: 고정석 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);

        // When: 사물함 이용 가능 여부 확인
        boolean cannotUseLocker = seatPass.cannotUseLocker();

        // Then: 사물함을 이용할 수 없어야 한다
        assertThat(cannotUseLocker).isTrue();
    }

    @Test
    @DisplayName("고정석 이용권은 사물함을 이용할 수 있어야 한다")
    void shouldReturnFalseWhenLockerAllowedForFixedPass() {
        // Given: 고정석 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 30000, 0.15);

        // When: 사물함 이용 가능 여부 확인
        boolean cannotUseLocker = seatPass.cannotUseLocker();

        // Then: 사물함을 이용할 수 없어야 한다
        assertThat(cannotUseLocker).isFalse();
    }

    @Test
    @DisplayName("동일한 기간과 타입의 사물함 이용권과 비교할 때 true를 반환해야 한다")
    void shouldReturnTrueForSameDurationAndTypeWithLockerPass() {
        // Given: 동일한 타입과 기간의 이용권 및 사물함 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);
        StudyCafeLockerPass lockerPass = mock(StudyCafeLockerPass.class);

        when(lockerPass.isSamePassType(StudyCafePassType.HOURLY)).thenReturn(true);
        when(lockerPass.isSameDuration(5)).thenReturn(true);

        // When: 동일한 타입과 기간인지 확인
        boolean result = seatPass.isSameDurationType(lockerPass);

        // Then: true를 반환해야 함
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("기간 또는 타입이 다를 경우 false를 반환해야 한다")
    void shouldReturnFalseForDifferentDurationOrTypeWithLockerPass() {
        // Given: 다른 기간의 이용권 및 사물함 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.WEEKLY, 7, 20000, 0.1);
        StudyCafeLockerPass lockerPass = mock(StudyCafeLockerPass.class);

        when(lockerPass.isSamePassType(StudyCafePassType.WEEKLY)).thenReturn(true);
        when(lockerPass.isSameDuration(5)).thenReturn(false);

        // When: 동일한 기간과 타입인지 확인
        boolean result = seatPass.isSameDurationType(lockerPass);

        // Then: false를 반환해야 함
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이용권의 타입이 일치하는 경우 true를 반환해야 한다")
    void shouldReturnTrueWhenPassTypeMatches() {
        // Given: 주 단위 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.WEEKLY, 7, 20000, 0.1);

        // When: 동일한 타입인지 확인
        boolean result = seatPass.isSamePassType(StudyCafePassType.WEEKLY);

        // Then: true를 반환해야 함
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이용권의 타입이 일치하지 않는 경우 false를 반환해야 한다")
    void shouldReturnFalseWhenPassTypeDoesNotMatch() {
        // Given: 고정석 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 30000, 0.15);

        // When: 타입이 다른 경우 확인
        boolean result = seatPass.isSamePassType(StudyCafePassType.HOURLY);

        // Then: false를 반환해야 함
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("할인된 가격을 정확하게 계산해야 한다")
    void shouldCalculateDiscountedPriceCorrectly() {
        // Given: 이용권 생성 (10% 할인)
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);

        // When: 할인된 가격 계산
        int discountPrice = seatPass.getDiscountPrice();

        // Then: 할인된 가격은 9000원이어야 함
        assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    @DisplayName("0% 할인인 경우 원래 가격과 동일해야 한다")
    void shouldReturnSamePriceWhenNoDiscount() {
        // Given: 할인율이 없는 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.0);

        // When: 할인된 가격 계산
        int discountPrice = seatPass.getDiscountPrice();

        // Then: 할인된 가격은 원래 가격과 동일해야 함
        assertThat(discountPrice).isEqualTo(0);
    }

    @Test
    @DisplayName("이용권의 타입을 정확하게 반환해야 한다")
    void shouldReturnCorrectPassType() {
        // Given: 시간 단위 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.HOURLY, 5, 10000, 0.1);

        // When: 이용권 타입 가져오기
        StudyCafePassType passType = seatPass.getPassType();

        // Then: 시간 단위 이용권 타입이어야 한다
        assertThat(passType).isEqualTo(StudyCafePassType.HOURLY);
    }

    @Test
    @DisplayName("이용권의 기간을 정확하게 반환해야 한다")
    void shouldReturnCorrectDuration() {
        // Given: 주 단위 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.WEEKLY, 7, 20000, 0.15);

        // When: 이용권 기간 가져오기
        int duration = seatPass.getDuration();

        // Then: 기간은 7일이어야 한다
        assertThat(duration).isEqualTo(7);
    }

    @Test
    @DisplayName("이용권의 가격을 정확하게 반환해야 한다")
    void shouldReturnCorrectPrice() {
        // Given: 고정석 이용권 생성
        StudyCafeSeatPass seatPass = StudyCafeSeatPass.of(StudyCafePassType.FIXED, 30, 50000, 0.2);

        // When: 이용권 가격 가져오기
        int price = seatPass.getPrice();

        // Then: 가격은 50000원이어야 한다
        assertThat(price).isEqualTo(50000);
    }
}
```