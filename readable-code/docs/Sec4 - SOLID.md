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


## LSP: 리스코프 치환 원칙


## ISP: 인터페이스 분리 원칙


## DIP: 의존 역전 원칙