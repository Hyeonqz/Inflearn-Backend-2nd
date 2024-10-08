package com.readable.code.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import com.readable.code.studycafe.tobe.exception.AppException;
import com.readable.code.studycafe.tobe.io.StudyCafeFileHandler;
import com.readable.code.studycafe.tobe.io.StudyCafeIOHandler;
import com.readable.code.studycafe.tobe.model.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.StudyCafePass;
import com.readable.code.studycafe.tobe.model.StudyCafePassType;

public class StudyCafePassMachine {
	// 공통 객체 필드로 변경 후 사용
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();

	public void run () {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			StudyCafePass selectedPass = getSelectedPass();
			Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);

			optionalLockerPass.ifPresentOrElse(
				lockerPass -> ioHandler.showPassOrderSummary(selectedPass, lockerPass),
				() -> ioHandler.showPassOrderSummary(selectedPass)
			);
		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafePass getSelectedPass () {
		StudyCafePassType passType = ioHandler.askPassTypeSelection();
		List<StudyCafePass> cafePassList = findPassCandidatesBy(passType);
		return ioHandler.askPassSelecting(cafePassList);
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
			boolean isLockerSelected = ioHandler.askLockerPass(lockerPassCandidate);
			if (isLockerSelected) {
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
