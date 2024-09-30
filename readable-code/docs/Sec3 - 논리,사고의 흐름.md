# 섹션3 논리, 사고의 흐름
## 뇌 메모리 적게 쓰기
책 추천 : '정리하는 뇌' <br>
인간의 범주화를 통해 정보를 처리하고 정리한다고 이야기를 한다 <br>

범주화를 통해 최소한의 기억 데이터를 통해 최대 효과를 낸다 <br>

책 추천 : 도둑맞은 집중력 <br>
뇌는 한 번에 한 가지 일 밖에 하지 못한다? <br>
멀티 태스킹? 뇌는 한 번에 한 가지 일 만 할 수 있다 <br>

그냥 저글링 일 뿐이다, 뇌는 CPU 랑 같다, 동시에 하는 것 처럼 보여도, 순차적으로 처리가 되는 것이다 <br>
멀티태스킹은 전체적인 집중력을 떨어뜨리고, 효율이 떨어질 수 있다 <br>

#### 인지적 경제성?
최소한의 인지를 통해 최대의 효율을 내보자는 의미이다 <br>

#### 즉, 뇌 메모리 적게 쓰기를 하기 위해서 어떻게 해야할까?
- 뇌 메모리에 적은 정보를 올리도록 코드를 작성할 수록 읽기 쉬운 코드가 된다.
- 읽는 사람의 뇌 메모리를 효과적으로 쓸 수 있도록 논리 구조 및 가독성을 높여야 한다.
- 이게 바로 읽기 좋은 코드를 만드는 방법이다.
- 인지적 경제성을 최대로 활용하자.


## Early return
```java
if (a>3) {
	doSomething1();
} else if(a<=3 && b>1) {
	doSomething2();
} else {
    doSomething3();	
}
```

위 코드를 읽게 되면 if 1번 조건을 거치고, 2번 조건을 거치고 최종적으로 else 를 거친다 <br>
마지막에는 앞선 정보들을 알게 모르게 기억을 하게 될 것이다 <br>

뇌 메모리를 적게 쓰기 위해서는 위 코드는 좋지 않다 <br>

여러 조건이 있는 if 문에서 뇌 메모리를 적게 쓰기 위해서는 early return 을 한다 <br>
```java
if (a>3) {
	doSomething1();
	return;
} 
if(a<=3 && b>1){
    doSomething2 ();
	return;
}  
doSomething3();	
}
```

위 처럼 return 을 해버리면 모든 조건문이 서로 다르게 때문에 신경 쓸 필요가 없다 <br>

* Early return 을 사용함으로 서 else 사용을 지양한다
  * else 를 아예 사용하지 말자가 아닌, 굳이? 라는 생각이 들면 지양 하는 코드를 짜는게 낫다.
    * switch~case 문 또한 포함이다 -> 명확한 반환이 있다면 써도 무방하다.

[변경전 코드]
```java
    if (doesUserChooseToPlantFlag(userActionInput)) {
				BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
				checkIfGameIsOver();
			} else if (doesUserChooseToOpenCell(userActionInput)) {
				if (landMineCell(selectedRowIndex, selectedColIndex)) {
					BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
					gameStatus = -1;
					continue;
				} else {
					open(selectedRowIndex, selectedColIndex);
				}
				checkIfGameIsOver();
			} else {
				System.out.println("잘못된 번호를 선택했습니다.");
			}
	}
```

위 코드를 early return 을 통해 else 를 지양하고, 더 깔끔한 코드로 만들 수 있다 <br>
위 코드를 통해 읽는 사람은 중첩 조건문 앞에 내용을 기억할 필요없이, 코드를 읽기 더 편리할 것이다 <br>

[개선된 코드]
```java
		if (doesUserChooseToPlantFlag(userActionInput)) {
			BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (landMineCell(selectedRowIndex, selectedColIndex)) {
				BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
				changeGameStatusToLose();
				return;
			}
			open(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}
		System.out.println("잘못된 번호를 선택했습니다.");
```

아직 길긴하지만 그래도 쪼금 더 깔끔해진 걸 확인할 수 있다.

<br>

## 사고의 depth 줄이기
보통 중첩 분기문, 중첩 반복문 에서 depth 가 깊어진다 <br>

```java
for(int i=0; i<20; i++) {
	for(int j=10; j<30; j++) {
        if(j>10) {
			// 구현 로직
        }		
	}
}
```

이럴 때는 메소드 추출을 통해 로직을 매끄럽게 만들 수 있다 <br>
무조건 메소드 추출을 하는게 아닌, 우리가 생각하기에 메소드 추출(=추상화)를 통해서 얻는 이득이 더 크다고 생각이 든다면 메소드 추출을 하는 것이다


### 무조건 depth 를 작게해라 가 아니다.
- 보이는 depth 를 줄이는 데에 급급한 것이 아니라, 추상화를 통한 사고 과정이 depth 를 줄이는 것이 중요
- 2중 중첩 구조로 표혀하는 것이 사고하는 데에 더 도움이 된다고 판단한다면, 메소드 추출보다 그대로 놔두는 것이 좋을 수도 있다.
  - 때로는 메소드를 분리하는 것이 더 혼선을 줄 수도 있다

### 사용할 변수는 가깝게 선언하기
```java
int i = 10;
// 코드 30줄

int j = i+30;
```

관련된 곳에 가까운 곳에 변수를 사용해야 잊혀지지 않고 기억에 상기될 수 있다 <br>
```java
int i = 10;
int j = i + 30;
```

위 처럼 사용하는 쪽에 가깝게 선언을 하자 <br>

```java
showGameStartComments();	
Scanner scanner = new Scanner(System.in);	
initializeGame();

// 로직 30줄

String cellInput = getCellInputFromUser(scanner);
String userActionInput = getUserActionFromUser(scanner);
```

Scanner 는 위에 선언 해두고, 실 사용은 아래에서 한다 <br>

아래와 같이 사용하면 가독성이 좋아진다.
```java
public static final Scanner SCANNER = new Scanner(System.in);

```

Scanner 를 상수로 만들어서 1회만 생성을 하고 재사용을 한다. <br>
파라미터를 줄일 수록, 가독성이 좋긴하다 <br>

추상화라는 의미에 맞게 메소드를 잘 설계 해야한다 <br>

<br>

## 공백 라인을 대하는 자세
* 공백 라인도 의미를 가진다.
  * 복잡한 로직의 의미 단위를 나누어 보여줌으로써, 읽는 사람에가 추가적인 정보를 전달할 수 있다.

```java
		while (true) {
			showBoard();
			// 공백 라인 추가
			if (doesUserWinTheGame()) {
				System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
				break;
			}
			if (doesUserLoseTheGame()) {
				System.out.println("지뢰를 밟았습니다. GAME OVER!");
				break;
			}

			String cellInput = getCellInputFromUser();
			String userActionInput = getUserActionFromUser();
            // 공백 라인 추가
			actOnCell(cellInput, userActionInput);
		}
```

<br>

## 부정어를 대하는 자세
[개선전 코드]
```java
if(!isLeftDirection()) {
	doSomething();
}
```

[개선후 코드]
```java
if(isRightDirection()) {
	doSomething();
}
if(isNotLeftDirection()) {
    // 로직	
}
```

부정연산자는 가독성이 조금 떨어진다, 그리고 메소드를 인지하고 뒤집어서 생각해야 하기 떄문에, 사고 과정이 2번 일어나야 한다 <br>

- 부정어구를 쓰지 않아도 되는 상황인지 체크하기
- 부정의 의미를 담은 다른 단어가 존재하는지 고민하기 or 부정어구로 메소드명 구성하기.
  - 부정 연산자(!) 의 가독성이 떨어진다

## 해피 케이스와 예외 처리
사람은, 해피 케이스에 몰두하는 경향이 있다? 내 코드는 무조건 정상 작동한다? <br>
예외 처리를 꼼꼼히 할수록 견고한 소프트웨어가 된다 <br>

1) 예외가 발생할 가능성 낮추기
2) 어떤 값의 검증이 필요한 부분은 주로 외부 세계와의 접점
   - 사용자 입력, 객체 생성자, 외부 서버의 요청 등
3) 의도한 예외와 예상하지 못한 예외를 구분하기
   - 사용자에게 보여줄 예외와, 개발자가 보고 처리해야 할 예외 구분.

### Null 을 대하는 자세
Kotlin 은 언어 단에서 Null 처리를 해주기 때문에 편하긴 하다.. <br>

- 항상 NullPointException 을 방지하는 방향으로 코드를 작성해야 한다(경각심 가지기)
- 메서드 설계시 return null 을 자제한다.
  - 만약 어렵다면, Optional 사용을 고민해 본다.

- Optional?
  - Optional 은 비싼 객체다. 꼭 필요한 상황에서 반환 타입에 사용한다.
  - Optional 을 파라미터로 받지 않도록 한다. 분기 케이스가 3개나 된다.
    - 애초에 Optional 은 return 타입으로 받게 설계가 되어 있음.
  - Optional 을 반환받았다면 최대한 빠르게 해소한다.
  - 어떻게 해소를 할까?
    - orElse(), orElseGet(), orElseThrow(), ifPresent(), ifPresentOrElse() 메소드를 활용하자.
    - 위 메소드를 잘 알고 사용해야 한다, 그렇지 않으면 성능상 문제가 발생한다 <br>
      - orElse() -> 확정된 값일 때 사용해야 한다.
      - orElseGet() -> null 인 경우 실행

<br>

 Optional 이 비싼이유? <br>
- 공간적 비용: Optional은 객체를 감싸는 컨테이너이므로 Optional 객체 자체를 저장하기 위한 메모리가 공간이 필요하다.
- 시간적 비용: Optional 안에 있는 객체를 얻기 위해서는 Optional 객체를 통해 접근해야 하므로 기본 접근 시간이 길다.

> e.printStackTrace(); 실무에서는 안티패턴이다, 보통 로그를 통해서 에러 해결한다

### 키워드 정리
- 뇌 메모리 적게 쓰기 (인지적 경제성)
- Early return, 사고의 Depth 줄이기(with 메소드 분리)
- 공백 라인, 부정어
- 해피 케이스, 예외 처리
- Stream API, Optional