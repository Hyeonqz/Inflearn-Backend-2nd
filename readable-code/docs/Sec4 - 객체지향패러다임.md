# Section4 - 객체 지향 패러다임
대표적인 프로그래밍 패러다임에는 절차 지향, 객체 지향, 함수형 이 있다 <br>
객체지향? 객체들 간의 협력을 통해 프로그래밍을 이루는 것 으로 한줄 요약 <br>

객체 Object, 추상화된 [데이터 + 코드] <br>

#### 캡추상다?
캡슐화, 추상화, 상속, 다형성? 이 4가지가 바로 객체지향의 특징 4가지 입니다 <br>

#### 관심사의 분리?
특정한 관심사에 따라서, 객체를 만들어내고, 기능을 만들고 책임을 나눈다? <br>
책임과 기능을 분리하면서 유지보수에 장점이 있다 <br>

위 관심사의 분리를 이야기하면, 높은 응집도, 낮은 결합도를 이야기 할 수 있다 <br>
각 관심사 끼리는 결합도가 낮아야하고, 관심사 끼리 응집도는 높아야 한다 <br>

관심사가 다른 것끼리는 서로 영향을 최소화 해야할 것이고, 관심사가 같은 것 끼리는 응집력을 높여 협력을 한다면 유지보수에 최대 이점이 될 것이다 <br><br>

## 객체 설계하기(1)
### 추상화 레벨
객체를 만드는 순간 경계가 생기고, 그 객체는 외부로 나가게 될 것이다 <br>
만든 객체에서 공개 메소드를 통해 외부와 협력을 한다 <br>

Object(=객체) 에는 비공개 필드(=데이터,변수) 가 있다 <br>
그리고 위 데이터를 다루기 위한 비공개 로직이 있다(기능 구현부) <br>

외부에서 위 객체와 협력하기 위해선 공개 메소드 선언부를 통해서 협력을 한다 <br>
즉 **객체의 책임**을 위 공개 메소드를 통해서 드러낼 수 있다 <br>

#### 객체를 추상화하기?
- 비공개 필드(데이터), 비공개 로직(코드)
- 공개 메소드 선언부를 통해 외부 세계와 소통
  - 각 메소드의 기능은 객체의 책임을 드러내는 창구
- 객체의 책임이 나누미에 따라 객체 간 협력이 발생한다.

#### 객체가 제공하는 것은?
- 절차 지향에서 잘 보이지 않았던 개념을 가시화
- 관심사가 한 군데로 모이기 때문에, 유지보수성이 올라간다.
  - ex) 객체 내부에서 객체가 가진 데이터의 유효성 검증 책임을 가질 수 있다.
- 여러 객체를 사용하는 입장에서는, 구체적인 구현에 신경 쓰지 않고 보다 높은 추상화 레벨에서 도메인 로직을 다룰 수 있다.

#### 새로운 객체를 만들 때 주의할점
- 1개의 관심사로 명확하게 책임이 정의되었는지 확인하기.
  - 메소드 추상화와 비슷하다. 
  - 객체를 만듦으로써 외부 세계와 어떤 소통을 하려고 하는지 생각해보자.


> 생성자, 정적 팩토리 메소드에서 유효성 검증이 가능하다?
- 즉 도메인에 특화된 검증 로직이 들어갈 수 있다
```java
public class Money {
	private long value;
	
	public Money(long value) {
		if(value<0)
			throw new IllegalArgumentException("돈은 0원 이상이어야 한다.");
		
		this.value = value;
    }
}
```

위 코드 처럼 객체 안에서 유효성 검증을 할 수 있다. <br>

> Setter 를 지향할 수 있다
- 데이터는 불변이 최고다. 변하는 데이터더라도 객체가 데이터를 핸들링 할 수 있어야 한다.
  - 사실 변하지 않으면, Side effect 가 생기지 않는다.
  - setter 는 살짝 폭력적인 메소드이다....
  - 물론 setter 가 무조건 안쓰는건 아니다. 상황에 따라 가끔은 사용할 떄가 있다.
- 객체 내부에서 외부 세계의 개입 없이, 자체적인 변경/가공으로 처리할 수 있는지를 확인해야 함.
- 만약 외부에서 가지고 있는 데이터 변경 요청을 해야하는 경우?
  - set 이름 보다는 update~ 처럼 메소드 네임에 의도를 잘드러나게 작성을 하자.

> getter 도 처음에는 사용을 자제 하길 바람.
- 반드시 필요한 경우에 추가하기.
- 외부에서 객체 내 데이터가 필요하다고 해서 getter 를 남발하는 것은 무례한 행동이다. 
- **'객체에 메시지를 보내는 것이 제일 적합하다.'** 
```java
// 메시지가 아닌 폭력적인 방법.
Person person = new Person();
if(person.getName().getAge().findAge() >= 19) {
	pass();
}

// 메시지 보내는 법
if(person.isAgeGreaterThanOrEqualTo(19)) {
	pass();
}
```

getter 또한 남발되면 캡슐화가 전혀 의미가 없어질 것이다 <br>

> 필드의 수는 적을수록 좋다.
- 불필요한 데이터가 많을수록 복잡도가 높아지고 대응할 변화가 많아진다.
- 필드 A 를 가지고 계산할 수 있는 A 필드가 있다면, 메소드 기능으로 제공
- 단, 미리 가공하는 것이 성능 상 이점이 있다면, 필드로 가지고 있는 것이 좋을 수도 있다.

## 객체 설계하기(2)
```java
public class Cell {

  private static final String FLAG_SIGN = "⚑";
  private static final String LAND_MINE_SIGN = "*";
  private static final String CLOSED_CELL_SIGN = "□";
  private static final String UNCHECKED_SIGN = "□";
  private static final String OPENED_CELL_SIGN = "◼️";
  private static final String EMPTY_SIGN = "◼️";

  private final String sign;
  private int nearbyLandMineCount;
  private boolean isLandMine;
  private boolean isFlagged;
  private boolean isOpened;

  private Cell (String sign, int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
    this.sign = sign;
    this.nearbyLandMineCount = nearbyLandMineCount;
    this.isLandMine = isLandMine;
    this.isFlagged = isFlagged;
    this.isOpened = isOpened;
  }

  // Cell 이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
  // Cell 의 상태? : 깃발 유무, open/closed, 사용자가 확인함.

  // 생성자 보다는, 정적 팩토리 메소드를 즐겨 사용한다.
  public static Cell of(String sign, int nearbyLandMineCount, boolean isLandMine) {
    return new Cell(sign, nearbyLandMineCount , isLandMine, false, false);
  }

  public static Cell create () {
    return of("",0,false);
  }

  public void turnOnLandMine () {
    this.isLandMine = true;
  }

  public void updateNearbyLandMineCount (int count) {
    this.nearbyLandMineCount = count;
  }

  // 공개 메소드로 외부와 소통
  public boolean equalsSign (String closedCellSign) {
    return this.sign.equals(closedCellSign);
  }

  public boolean isClosed () {
    return CLOSED_CELL_SIGN.equals(this.sign);
  }
  // 1. Getter/Setter 는 처음부터 만들지 말자.

  public static Cell ofClosed () {
    return of(CLOSED_CELL_SIGN,0,false);
  }

  public boolean doesNotClosed () {
    return !isClosed();
  }

  public void flag () {
    this.isFlagged = true;
  }

  public boolean isChecked () {
    return isFlagged || isOpened;
  }

  public boolean isLandMine () {
    return this.isLandMine;
  }

  public void open () {
    this.isOpened = true;
  }

  public boolean isOpened () {
    return isOpened;
  }

  public boolean hasLandMineCount () {
    return this.nearbyLandMineCount != 0;
  }

  public String getSign () {
    return sign;
  }
}

```

위 챕터는 어려운 부분이 좀 있어, 지금은 최소한의 이해만 해두고 다시 강의를 들을 예정 <br>
객체를 존중해준다는 마인드로 로직을 작성해야 한다, 막무가내로 내 객체에 필드를 듸지는 폭력적인 getter 를 지양하자!!