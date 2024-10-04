# SOLID
- SRP: Single Responsibility Principle
- OCP: Open-Closed Principle
- LSP: Liskov Substitution Principle
- ISP: Interface Segregation Principle
- DIP: Dependency Inversion Principle

<br>

## SRP: 단일 책임 원칙
- 하나의 클래스는 단 한 가지의 변경 이유만을 가져야 한다.
    - 변경 이유? = 책임
- 객체가 가진 공개 메소드, 필드, 상수 등은 해당 객체의 단일 책임에 의해서만 변경 되는가?
- 관심사의 분리 = 책임의 분리 -> 하나의 책임만 가져야 한다.
- 높은 응집도, 낮은 결합도(=의존성)

'책임' 을 인식하고 구별하는 눈이 필요하다 <br>

```java
public class GameApplication {

	public static void main (String[] args) {
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.run();
	}
}
```

기존의 모든 코드들이 한 객체에서 실행이 되었는데, 역할을 분리를 통하여 실행할 클래스, 기능 클래스를 나눴다 <br>
ex) 출력,입력에 따른 객체 분리, 

<br>

## OCP: 개방 폐쇄 원칙
- 확장에는 열려 있고, 수정에는 닫혀 있어야 한다.
  - 기존 코드의 변경 없이, 시스템의 기능을 확장할 수 있어야 한다.
- 추상화와 다형성을 활용해서 OCP 를 지킬 수 있다.
  - interface, abstract 활용

#### 게임의 난이도를 변경할 수 있어야 한다 -> 새로운 요구사항. 
[인터페이스 활용]
```java
// 선언부
public interface GameLevel {
	// 추상화를 다이렉트로 표현한 구조가 인터페이스 이다.
	// 인터페이스가 가지는 스펙들이 메소드 선언부를 선언한다.
	int getRowSize();
	int getColSize();
	int getLandMineCount();
}

// 구현부
public class VeryBeginner implements GameLevel{

  @Override
  public int getRowSize () {
    return 4;
  }

  @Override
  public int getColSize () {
    return 5;
  }

  @Override
  public int getLandMineCount () {
    return 2;
  }

}

public class Beginner implements GameLevel{
  @Override
  public int getRowSize () {
    return 8;
  }

  @Override
  public int getColSize () {
    return 10;
  }

  @Override
  public int getLandMineCount () {
    return 10;
  }

}

public GameBoard (GameLevel gameLevel) {
  int colSize = gameLevel.getColSize();
  int rowSize = gameLevel.getRowSize();
  board = new Cell[rowSize][colSize];

  landMineCount = gameLevel.getLandMineCount();
}

public class GameApplication {

  public static void main (String[] args) {
    GameLevel gameLevel = new Advanced();

    Minesweeper mineSweeper = new Minesweeper(gameLevel);
    mineSweeper.run();
  }
}
```

위 처럼, 선언부를 선언해두고, 구현부를 통해 구체적인 구현을 구한다 <br>
추상화라는 내용 자체가 이해가 하기 어려워서 여러 예시를 보고 배우면서 익숙해지기 위해 노력하고 있습니다 <br>

## LSP: 리스코프 치환 원칙
- 상속 구조에서, 부모 클래스의 인스턴스를 자식 클래스으 인스턴스로 치환할 수 있어야 한다.
  - 자식 클래스는 부모 클래스의 책임을 준수하며, 부모 클래스으 행동을 변경하지 않아야 한다.
- LSP 를 위반하면, 상속 클래스를 사용할 때 오동작, 예상 밖의 예외가 발생하거나, 이를 방지하기 위한 불필요한 타입 체크가 동반될 수 있다.

부모쪽의 기능보다는 자식쪽에 기능이 조금 더 많다 <br>

```java
// 자식 클래스에서 구현해서 사용하겠다는 뜻, 상위 클래스에는 스펙만 가지고 있다
public abstract void turnOnLandMine (); 
```

공통된 메소드는 상위 클래스에서 스펙 + 구현을 통해 공통으로 사용할 수 있게 한다 <br>
프로세스를 생각해보면 이해가 더 쉽다 <br>






## ISP: 인터페이스 분리 원칙










## DIP: 의존 역전 원칙