# Section5 - 객체 지향 적용하기

## 상속과 조합
![img_2.png](img/img_2.png) <br>

- 상속보다 조합을 사용하자!
- 상속은 시멘트처럼 굳어지는 구조다. 즉 수정이 어렵다
  - 부모와 자식의 결합도가 높다.
  - 상속 디자인 자체가 결합도가 높은 디자인이다.
  - 상속을 무조건 사용하지 말자는 아니고, 상속이 더 큰 효율(=절대 변하지않는?) 을 가져다 주는 구조가 아니면 굳이 안쓰는게 좋다.
- 🌟조합과 인터페이스를 활용하는 것이 유연한 구조
  - 상속을 통한 코드의 중복 제거가 주는 이점보다, 중복이 생기더라도 유연한 구조 설계가 주는 이점이 더 크다.

```java
public abstract class Cell {
	protected boolean isFlagged;
	protected boolean isOpened;

}

public class EmptySell extends Cell {
	protected static final String EMPTY_SIGN = "◼";

	@Override
	public String getSign () {
		if(isOpened) { // 부모 필드 사용
			return EMPTY_SIGN;
		}
		if(isFlagged) { // 부모 필드 사용
			return FLAG_SIGN;
		}
		return UNCHECKED_SIGN;
	}
}
```

위 같은 코드는 부모, 자식 간의 높은 결합도를 보여주는 코드이다 <br>
부모 필드에 변경이 생긴다면? 모든 자식 모듈 또한 변경이 되어야 할 것이다 <br>

위 같은 코드를 상속이 아닌, '조합' 을 사용해서 풀어가 보자 <br>
```java
public class CellState {

	private boolean isFlagged;
	private boolean isOpened;

	public CellState (boolean isFlagged, boolean isOpened) {
		this.isFlagged = isFlagged;
		this.isOpened = isOpened;
	}

	public static CellState initialize() {
		return new CellState(false, false);
	}

	public void flag () {
		this.isFlagged = true;
	}

	public boolean isChecked () {
		return isFlagged || isOpened;
	}

	public void open () {
		this.isOpened = true;
	}

	public boolean isOpened () {
		return isOpened;
	}

	public boolean isFlagged () {
		return isFlagged;
	}

}
```

상속은 보통, 조합을 통해 대부분 해결을 할 수 있다, 실무에서는 복잡한 비즈니스가 많으므로 최대한 상속대신 조합을 사용하자 <br>

## Value Object
- 유명한 추상화 기법이다.
- 도메인의 어떤 개념을 추상화하여 표현한 값 객체
- 값으로 취급하기 위해서, 불변성, 동등성, 유효성 검증 등을 보장해야 한다.
  - 불변성: final 필드, setter 금지
  - 동등성: 메모리가 주소가 다른, 즉 서로 다른 인스턴스 여도(동일성이 달라도), 내부의 값이 같으면 같은 값 객체로 취급한다.
    - equals() & hashCode() 재정의 필요.
    - ex) 만원짜리 5개가 있다, 각 지폐는 고유의 일련번호가 있다. 이거랑 비슷한 느낌이 있다.
      - 일련번호(=메모리 주소)가 다르지만, 고유의 역할은 다 똑같다. 서로 다른 인스턴스여도, 결국 내부 값(=필드) 는 같은 인스턴스이다. ex) DTO, Entity
  - 유효성 검증: 객체가 생성되는 시점에 값에 대한 유효성을 보장하기
  
- **VO vs Entity**
- **1차 조건**
- 식별자가 있다면? **Entity**
  - 식별자가 아닌 필드의 값이 달라도, 식별자가 같으면 동등한 객체로 취급한다.
  - equals() & hashCode() 도 식별자 필드만 가지고 재정의할 수 있다.
  - 식별자가 같은데 식별자가 아닌 필드의 값이 서로 다른 두 인스턴스가 있다면, 같은 Entity 가 시간이 지남에 따라 변화한 것으로 이해할 수도 있다.
  

  - 식별자가 없다면? **VO(=DTO)**
    - 식별자가 없어 내부을 모든 값이 다 같아야 동등한 객체로 취급한다.
      - 개념적으로 전체 필드가 다같이 식별자 역할을 한다고 생각해도 된다.

```java
public class RelativePosition {
	private final int deltaRow;
	private final int deltaCol;

	public RelativePosition (int deltaRow, int deltaCol) {
		this.deltaRow = deltaRow;
		this.deltaCol = deltaCol;
	}

	public static RelativePosition of(int deltaRow, int deltaCol) {
		return new RelativePosition(deltaRow, deltaCol);
	}

	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RelativePosition that = (RelativePosition)o;
		return deltaRow == that.deltaRow && deltaCol == that.deltaCol;
	}

	@Override
	public int hashCode () {
		return Objects.hash(deltaRow, deltaCol);
	}

}
```

- 동등성 보장을 위한 equals & hashCode 를 만들어 준다.

## 일급 컬렉션
- 다른 요소에게 사용 가능한 모든 연산을 지원하는 요소
  - 변수로 할당 될 수 있다.
  - 파라미터로 전달될 수 있다
  - 함수의 결과로 반환될 수 있다.
    - ex) 일급 함수
      - 함수형 프로그래밍 언어에서, 함수는 일급 시민이다
      - 함수는 변수에 할당될 수 있고, 인자로 전달될 수 있고, 함수의 결과로 함수가 반환될 수도 있다.

- 컬렉션을 포장하면서, 컬렉션 만을 유일하게 필드로 가지는 객체
  - 컬렉션을 다른 객체와 동둥한 레벨로 다루기 위함
  - 단 하나의 컬렉션 필드만을 가진다.
- 컬렉션을 추상화하며 의미를 담을 수 있고, 가공 로직의 보금자리가 생긴다.
  - 가공 로직에 대한 테스트도 작성할 수 있다.
- 만약 getter 로 컬렉션을 반환할 일이 생긴다면
  - 외부 조작을 피하기 위해 꼭 새로운 컬렉션으로 만들어서 반환해주자.

```java
public class Cells {
	// 1급 컬렉션 -> 필드가 1개다
	
	private final List<Cell> cells;

	public Cells (List<Cell> cells) {
		this.cells = cells;
	}
	
	public static Cells of(List<Cell> cells) {
		return new Cells(cells);
	}
	
	public static Cells from(Cell[][] cells) {
		List<Cell> list = Arrays.stream(cells)
			.flatMap(Arrays::stream)
			.toList();
		return Cells.of(list);
	}
	
	public boolean isAllChecked() {
		return cells.stream()
			.allMatch(Cell::isChecked);
	}

}
```

위 처럼 적용을 할 수 있다. 아직은 이해하기 어려운 내용이였지만, 위 내용을 다듬어서 잘 사용할 수 있다면 강력한 무기가 될 것이라고 생각한다.

<br>

## Enum 의 특성과 활용
- Enumerate 의 약자로, 열거형 이라고 불린다.
- Enum 은 상수의 집합이며, 상수와 관련된 로직을 담을 수 있는 공간이다.
  - 상태와 행위를 한 곳에서 관리할 수 있는 추상화된 객체
- 특정 도메인 개념에 대해 그 종류와 기능을 명시적으로 표현해줄 수 있다.
- 만약 변경이 정말 잦은 개념은, Enum 보다 DB 로 관리하는 것이 나을 수 있다.

Enum 은 코드에 박혀있는 값이기 때문에, 월화수목금, 봄여름가을겨울 처럼 절대 변하지 않는 값들을 저장하는게 좋다 <br>

```java
public enum CellSnapshotStatus {
	EMPTY("빈 셀"),
	FLAG("깃발"),
	LAND_MINE("지뢰"),
	NUMBER("숫자"),
	UNCHECKED("확인전")
	;

	private final String description;

	CellSnapshotStatus (final String description) {
		this.description = description;
	}
}

```

<br>

## 다형성 활용하기
반복적인 if 문을 제거해보자 <br>

```java
	private String decideCellSignFrom (CellSnapshot cellSnapshot) {
		CellSnapshotStatus status = cellSnapshot.getStatus();
		if(status == CellSnapshotStatus.EMPTY)
			return EMPTY_SIGN;
		if(status == CellSnapshotStatus.FLAG)
			return FLAG_SIGN;
		if(status == CellSnapshotStatus.LAND_MINE)
			return LAND_MINE_SIGN;
		if(status == CellSnapshotStatus.NUMBER)
			return String.valueOf(cellSnapshot.getNearbyLandMineCount());
		if(status == CellSnapshotStatus.UNCHECKED)
			return UNCHECKED_SIGN;

		throw new IllegalArgumentException("확인할 수 없는 셀 입니다");
	}
```

위 코드의 여러개의 if 문이 보기가 싫다. 다형성을 사용해서 리팩토링을 해보자 <br>

[추상부]
```java
// 메소드의 스펙을 가지고 있어야 한다.
public interface CellSignProvidable {
	String provide(CellSnapshot cellSnapshot);
}
```

[구현체]
```java
public class FlagCellSignProvider implements CellSignProvidable{
	private static final String FLAG_SIGN = "⚑";

	@Override
	public String provide (CellSnapshot cellSnapshot) {
		return FLAG_SIGN;
	}

	@Override
	public boolean supports (CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(CellSnapshotStatus.FLAG);
	}

}

public class LandMineCellSignProvider implements CellSignProvidable{
  private static final String LAND_MINE_SIGN = "*";

  @Override
  public String provide (CellSnapshot cellSnapshot) {
    return LAND_MINE_SIGN;
  }

  @Override
  public boolean supports (CellSnapshot cellSnapshot) {
    return cellSnapshot.isSameStatus(CellSnapshotStatus.LAND_MINE);
  }

}
```

위와 같은 구현체 총 5개를 준비한다. 그리고 실제 메소드에서 사용은 아래와 같다
```java
	private String decideCellSignFrom (CellSnapshot cellSnapshot) {
		CellSnapshotStatus status = cellSnapshot.getStatus();

		List<CellSignProvidable> cellSignProvidables = List.of(
			new EmptySellSignProvider(),
			new FlagCellSignProvider(),
			new LandMineCellSignProvider(),
			new NumberCellSignProvider(),
			new UncheckedCellSignProvider()
		);

		return cellSignProvidables.stream()
			.filter(provider -> provider.supports(cellSnapshot))
			.findFirst()
			.map(provider -> provider.provide(cellSnapshot))
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다"));
	}
```

List 에 인터페이스 에 따른 클래스를 다 담고, 스트림을 돌려서 찾아내는 로직이다 <br>

Enum 을 활용하여 아래와 같이 다형성을 구현할 수 도 있다 
```java
public enum CellSignProvider implements CellSignProvidable {
	EMPTY(CellSnapshotStatus.EMPTY) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return EMPTY_SIGN;
		}
	},
	FLAG(CellSnapshotStatus.FLAG) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return FLAG_SIGN;
		}
	},
	LAND_MINE(CellSnapshotStatus.LAND_MINE) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return LAND_MINE_SIGN;
		}
	},
	NUMBER(CellSnapshotStatus.NUMBER) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return String.valueOf(cellSnapshot.getNearbyLandMineCount());
		}
	},
	UNCHECKED(CellSnapshotStatus.UNCHECKED) {
		@Override
		public String provide (CellSnapshot cellSnapshot) {
			return UNCHECKED_SIGN;
		}
	}
	;

	private static final String EMPTY_SIGN = "◼";
	private static final String FLAG_SIGN = "⚑";
	private static final String LAND_MINE_SIGN = "*";
	private static final String UNCHECKED_SIGN = "□";

	private final CellSnapshotStatus status;

	CellSignProvider (CellSnapshotStatus cellSnapshotStatus) {
		this.status = cellSnapshotStatus;
	}

	@Override
	public String provide (CellSnapshot cellSnapshot) {
		return "";
	}

	@Override
	public boolean supports (CellSnapshot cellSnapshot) {
		return cellSnapshot.isSameStatus(status);
	}

	public static String findCellSignFrom(CellSnapshot cellSnapshot) {
		return Arrays.stream(values())
			.filter(provider -> provider.supports(cellSnapshot))
			.findFirst()
			.map(provider -> provider.provide(cellSnapshot))
			.orElseThrow(() -> new IllegalArgumentException("확인할 수 없는 셀 입니다."));
	}
}
```

변하는 것과 변하지 않는 것을 분리하여 추상화하고, OCP 를 지키는 구조가 최고의 추상화 구조이다 

### 숨겨져 있는 도메인 개념 도출하기
- 도메인 지식은 만드는 것이 아니라 발견하는 것
- 객체 지향은 현실을 100% 반영하는 도구가 아니라, 흉내내는 것이다
  - 현실 세계에서 쉽게 인지하지 못하는 개념도 도출해서 사용해야 할 때가 있다
- 설계할 때는 근시적, 거시적 관점에서 최대한 미래를 예측하고, 시간이 지나 만약 틀렸다는 것을 인지하면, 언제든 돌아올 수 있도록 코드를 만들어야 한다.
  - 완벽한 설계는 없다. 그 당시의 최선이 있을뿐....


### 키워드 정리
1) 상속과 조합 (상속보다는 조합 사용하자)
2) Value Object, Entity (불변성, 동등성, 유효성 검증)
3) 일급 컬렉션
4) Enum
5) 추상화와 다형성 활용하여 반복되는 if 문 제거, OCP 지키기
6) 숨겨져 있는 도메인 개념 도출하기.