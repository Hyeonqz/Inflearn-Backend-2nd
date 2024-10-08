package com.readable.code.studycafe.tobe.io.provider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.readable.code.studycafe.tobe.model.pass.StudyCafePassType;
import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import com.readable.code.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import com.readable.code.studycafe.tobe.provider.LockerPassProvider;

public class LockerPassFilerReader implements LockerPassProvider {
	private static final String LOCKER_LIST_CSV_PATH = "src/main/resources/cleancode/studycafe/locker.csv";
	@Override
	public StudyCafeLockerPasses getLockerPasses () {
		try {
			List<String> lines = Files.readAllLines(Paths.get(LOCKER_LIST_CSV_PATH));

			List<StudyCafeLockerPass> lockerPasses = new ArrayList<>();
			for (String line : lines) {
				String[] values = line.split(",");
				StudyCafePassType studyCafePassType = StudyCafePassType.valueOf(values[0]);
				int duration = Integer.parseInt(values[1]);
				int price = Integer.parseInt(values[2]);

				StudyCafeLockerPass lockerPass = StudyCafeLockerPass.of(studyCafePassType, duration, price);
				lockerPasses.add(lockerPass);
			}

			return StudyCafeLockerPasses.of(lockerPasses);
		} catch (IOException e) {
			throw new RuntimeException("파일을 읽는데 실패했습니다.", e);
		}
	}

}
