package com.readable.code.studycafe.tobe.model.order;

import java.util.Optional;

import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPass;
import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;

public class StudyCafePassOrder {
	private final StudyCafeSeatPass studyCafeSeatPass;
	private final StudyCafeLockerPass studyCafeLockerPass;

	public StudyCafePassOrder (StudyCafeSeatPass studyCafeSeatPass, StudyCafeLockerPass studyCafeLockerPass) {
		this.studyCafeSeatPass = studyCafeSeatPass;
		this.studyCafeLockerPass = studyCafeLockerPass;
	}

	public int getDiscountPrice () {
		return studyCafeSeatPass.getDiscountPrice();
	}

	public int getTotalPrice () {
		int lockerPassPrice = studyCafeLockerPass != null ? studyCafeLockerPass.getPrice() : 0 ;
		int totalPassPrice = studyCafeSeatPass.getPrice() + lockerPassPrice;
		return totalPassPrice - getDiscountPrice();

	}

	public StudyCafeSeatPass getSeatPass () {
		return this.studyCafeSeatPass;
	}

	public static StudyCafePassOrder of(StudyCafeSeatPass studyCafeSeatPass, StudyCafeLockerPass studyCafeLockerPass) {
		return new StudyCafePassOrder(studyCafeSeatPass, studyCafeLockerPass);
	}

	public Optional<StudyCafeLockerPass> getLockerPass () {
		return Optional.ofNullable(this.studyCafeLockerPass);
	}

}
