package com.readable.code.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import com.readable.code.studycafe.tobe.exception.AppException;
import com.readable.code.studycafe.tobe.io.InputHandler;
import com.readable.code.studycafe.tobe.io.OutputHandler;
import com.readable.code.studycafe.tobe.io.StudyCafeFileHandler;
import com.readable.code.studycafe.tobe.model.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.StudyCafePass;
import com.readable.code.studycafe.tobe.model.StudyCafePassType;

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
		return studyCafePasses.stream()
			.filter(studyCafePass -> studyCafePass.isSamePassType(studyCafePassType))
			.toList();
	}

	private Optional<StudyCafeLockerPass> selectLockerPass (StudyCafePass selectedPass) {
		if (selectedPass.cannotUserLocker()) {
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
			.filter(pass::isSameDurationType)
			.findFirst()
			.orElse(null);
	}

}
