package com.readable.code.studycafe.tobe.io;

import java.util.List;

import com.readable.code.studycafe.tobe.model.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.StudyCafePass;
import com.readable.code.studycafe.tobe.model.StudyCafePassType;

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
