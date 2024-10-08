package com.readable.code.studycafe.tobe.io.provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.readable.code.studycafe.tobe.model.pass.StudyCafePassType;
import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPass;
import com.readable.code.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import com.readable.code.studycafe.tobe.provider.SeatPassProvider;

public class SeatPassFileReader implements SeatPassProvider {
	private static final String SEAT_PASS_CSV = "src/main/resources/cleancode/studycafe/pass-list.csv";

	@Override
	public StudyCafeSeatPasses getSeatPasses () {
		try {
			List<String> lines = Files.readAllLines(Paths.get(SEAT_PASS_CSV));
			List<StudyCafeSeatPass> studyCafeSeatPasses = new ArrayList<>();
			for (String line : lines) {
				String[] values = line.split(",");
				StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
				int duration = Integer.parseInt(values[1]);
				int price = Integer.parseInt(values[2]);
				double discountRate = Double.parseDouble(values[3]);

				StudyCafeSeatPass studyCafeSeatPass = StudyCafeSeatPass.of(studyCafePassType, duration, price, discountRate);
				studyCafeSeatPasses.add(studyCafeSeatPass);
			}

			return StudyCafeSeatPasses.of(studyCafeSeatPasses);
		} catch (IOException e) {
			throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
		}
	}

}