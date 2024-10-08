package com.readable.code.studycafe.tobe;

import java.util.List;
import java.util.Optional;

import com.readable.code.studycafe.tobe.exception.AppException;
import com.readable.code.studycafe.tobe.io.StudyCafeFileHandler;
import com.readable.code.studycafe.tobe.io.StudyCafeIOHandler;
import com.readable.code.studycafe.tobe.model.order.StudyCafePassOrder;
import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPass;
import com.readable.code.studycafe.tobe.model.pass.StudyCafePassType;
import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPasses;

public class StudyCafePassMachine {
	private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
	private final StudyCafeIOHandler ioHandler = new StudyCafeIOHandler();

	public void run () {
		try {
			ioHandler.showWelcomeMessage();
			ioHandler.showAnnouncement();

			StudyCafeSeatPass selectedPass = getSelectedPass();
			Optional<StudyCafeLockerPass> optionalLockerPass = selectLockerPass(selectedPass);
			StudyCafePassOrder passOrder = StudyCafePassOrder.of(selectedPass, optionalLockerPass.orElse(null));

			ioHandler.showPassOrderSummary(passOrder);
		} catch (AppException e) {
			ioHandler.showSimpleMessage(e.getMessage());
		} catch (Exception e) {
			ioHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
		}
	}

	private StudyCafeSeatPass getSelectedPass () {
		StudyCafePassType passType = ioHandler.askPassTypeSelection();
		List<StudyCafeSeatPass> cafePassList = findPassCandidatesBy(passType);
		return ioHandler.askPassSelecting(cafePassList);
	}

	private List<StudyCafeSeatPass> findPassCandidatesBy (StudyCafePassType studyCafePassType) {
		StudyCafeSeatPasses allPasses = studyCafeFileHandler.readStudyCafePasses();
		return allPasses.findPassBy(studyCafePassType);
	}

	private Optional<StudyCafeLockerPass> selectLockerPass (StudyCafeSeatPass selectedPass) {
		if (selectedPass.cannotUserLocker()) {
			return Optional.empty();
		}

		Optional<StudyCafeLockerPass> lockerPassCandidate = findLockerPassCandidateBy(selectedPass);

		if (lockerPassCandidate.isPresent()) {
			StudyCafeLockerPass lockerPass = lockerPassCandidate.get();
			boolean isLockerSelected = ioHandler.askLockerPass(lockerPass);
			if (isLockerSelected) {
				return Optional.of(lockerPass);
			}
		}
		return Optional.empty();
	}

	private Optional<StudyCafeLockerPass> findLockerPassCandidateBy (StudyCafeSeatPass pass) {
		StudyCafeLockerPasses allLockerPasses = studyCafeFileHandler.readLockerPasses();
		return allLockerPasses.findLockerPassBy(pass);
	}

}
