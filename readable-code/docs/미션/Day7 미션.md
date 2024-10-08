# Day7 미션
> 미션 내용	"[섹션 7. 리팩토링 연습]의 ""연습 프로젝트 소개"" 강의를 보고, <br>
'스터디 카페 이용권 선택 시스템' 프로젝트에서 지금까지 배운 내용을 기반으로 리팩토링을 진행해 봅시다.

## 리팩토링 포인트
- 추상화 레벨
- 객체로 묶어볼만한 것은 없는지
- 객체지향 패러다임에 맞게 객체들이 상호 협력하고 있는지
- SRP : 책임에 따라 응집도 있게 객체가 잘 나뉘어져 있는지
- DIP : 의존 관계 역전을 적용할 만한 곳은 없는지
- 일급 컬렉션

1) 리팩토링 한 단계마다, 그 이유를 설명할 수 있어야 한다.

위 포인트를 리마인드 시키면서 리팩토링을 진행해보자 <br>

고민 끝에 Sec7 강의를 참고해 가며 리팩토링을 진행하였습니다 <br>

### 중복 제거 & 메소드 추출
```java
public class StudyCafePassMachine {

	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();
	// 공통 객체 필드로 변경 후 사용
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

	public void run () {
		try {
			outputHandler.showWelcomeMessage();
			outputHandler.showAnnouncement();

			StudyCafePass selectedPass = getSelectedPass();
			Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			optionalLockerPass.ifPresentOrElse(
				lockerPass -> outputHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> outputHandler.showPassOrderSummary(selectedPass)
			);
		} catch (AppException e) {
			outputHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass getSelectedPass () {
		outputHandler.askPassTypeSelection();
		StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

		List<StudyCafePass> cafePassList = findPassCandidatesBy(studyCafePassType);

		outputHandler.showPassListForSelection(cafePassList);
		return inputHandler.getSelectPass(cafePassList);
	}

	private List<StudyCafePass> findPassCandidatesBy (StudyCafePassType studyCafePassType) {
		List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePasses();
		List<StudyCafePass> cafePassList = studyCafePasses.stream()
			.filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
			.toList();
		return cafePassList;
	}

	private Optional<StudyCafeLockerPass> selectLockerPass (StudyCafePass selectedPass) {
		if (selectedPass.getPassType() != StudyCafePassType.FIXED) {
			return Optional.empty();
		}

		StudyCafeLockerPass lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate != null) {
			outputHandler.askLockerPass(lockerPassCandidate);
			boolean lockerSelection = inputHandler.getLockerSelection();

			if (lockerSelection) {
				return Optional.of(lockerPassCandidate);
			}
		}
		return Optional.empty();
	}

	private StudyCafeLockerPass findLockerPassCandidateBy (StudyCafePass pass) {
		List<StudyCafeLockerPass> allLockerPasses = studyCafeFileHandler.readLockerPasses();

		return  allLockerPasses.stream()
			.filter(lockerPass ->
				lockerPass.getPassType() == pass.getPassType()
					&& lockerPass.getDuration() == pass.getDuration()
			)
			.findFirst()
			.orElse(null);
	}

}
```

### 객체의 책임과 응집도
[I/O 통합]
```java
public class StudyCafeIOHandler {

	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();

	public void showWelcomeMessage() {
		outputHandler.showWelcomeMessage();
	}

	public void showAnnouncement() {
		outputHandler.showAnnouncement();
	}

	public void showPassOrderSummary(StudyCafePass studyCafePass, StudyCafeLockerPass studyCafeLockerPass) {
		outputHandler.showPassOrderSummary(studyCafePass, studyCafeLockerPass);
	}

	public void showPassOrderSummary(StudyCafePass studyCafePass) {
		outputHandler.showPassOrderSummary(studyCafePass);
	}

	public void showSimpleMessage(String message) {
		outputHandler.showSimpleMessage(message);
	}

	public StudyCafePassType askPassTypeSelection () {
		outputHandler.askPassTypeSelection();
		return inputHandler.getPassTypeSelectingUserAction();
	}

	public StudyCafePass askPassSelecting (List<StudyCafePass> cafePassList) {
		outputHandler.showPassListForSelection(cafePassList);
		return inputHandler.getSelectPass(cafePassList);
	}

	public boolean askLockerPass (StudyCafeLockerPass lockerPassCandidate) {
		outputHandler.askLockerPass(lockerPassCandidate);
		return inputHandler.getLockerSelection();
	}

}
```

중간에 브릿지 객체를 두어, 실 사용이 필요할 때 Input, Output 객체 2개를 사용하는게 아닌, Bridge 객체를 사용한다 <br>

### 일급 컬렉션
한 객체에 대해 전용으로 사용하는 객체(=자료구조) 를 의미함. 

[일급 컬렉션 대상 객체]
```java
public class StudyCafePass {

    private final StudyCafePassType passType;
    private final int duration;
    private final int price;
    private final double discountRate;

    private StudyCafePass(StudyCafePassType passType, int duration, int price, double discountRate) {
        this.passType = passType;
        this.duration = duration;
        this.price = price;
        this.discountRate = discountRate;
    }

    public static StudyCafePass of(StudyCafePassType passType, int duration, int price, double discountRate) {
        return new StudyCafePass(passType, duration, price, discountRate);
    }

}
```

[일급 컬렉션]
```java
public class StudyCafePasses {
	private final List<StudyCafePass> passes;

	public StudyCafePasses (List<StudyCafePass> passes) {
		this.passes = passes;
	}

	public static StudyCafePasses of (List<StudyCafePass> passes) {
		return new StudyCafePasses(passes);
	}

	public List<StudyCafePass> findPassBy (StudyCafePassType studyCafePassType) {
		return passes.stream()
			.filter(studyCafePass -> studyCafePass.isSamePassType(studyCafePassType))
			.toList();
	}

}

```