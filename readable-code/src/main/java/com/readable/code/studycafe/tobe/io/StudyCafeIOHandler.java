package com.readable.code.studycafe.tobe.io;

import java.util.List;

import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPass;
import com.readable.code.studycafe.tobe.model.pass.StudyCafePassType;

public class StudyCafeIOHandler {

	private final InputHandler inputHandler = new InputHandler();
	private final OutputHandler outputHandler = new OutputHandler();

	public void showWelcomeMessage() {
		outputHandler.showWelcomeMessage();
	}

	public void showAnnouncement() {
		outputHandler.showAnnouncement();
	}

	public void showPassOrderSummary(StudyCafeSeatPass studyCafeSeatPass, StudyCafeLockerPass studyCafeLockerPass) {
		outputHandler.showPassOrderSummary(studyCafeSeatPass, studyCafeLockerPass);
	}

	public void showPassOrderSummary(StudyCafeSeatPass studyCafeSeatPass) {
		outputHandler.showPassOrderSummary(studyCafeSeatPass);
	}

	public void showSimpleMessage(String message) {
		outputHandler.showSimpleMessage(message);
	}

	public StudyCafePassType askPassTypeSelection () {
		outputHandler.askPassTypeSelection();
		return inputHandler.getPassTypeSelectingUserAction();
	}

	public StudyCafeSeatPass askPassSelecting (List<StudyCafeSeatPass> cafePassList) {
		outputHandler.showPassListForSelection(cafePassList);
		return inputHandler.getSelectPass(cafePassList);
	}

	public boolean askLockerPass (StudyCafeLockerPass lockerPassCandidate) {
		outputHandler.askLockerPass(lockerPassCandidate);
		return inputHandler.getLockerSelection();
	}

}
