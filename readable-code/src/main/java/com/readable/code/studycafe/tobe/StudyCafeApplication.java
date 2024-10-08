package com.readable.code.studycafe.tobe;

import com.readable.code.studycafe.tobe.io.provider.LockerPassFilerReader;
import com.readable.code.studycafe.tobe.io.provider.SeatPassFileReader;
import com.readable.code.studycafe.tobe.provider.LockerPassProvider;
import com.readable.code.studycafe.tobe.provider.SeatPassProvider;

public class StudyCafeApplication {

    public static void main(String[] args) {
        SeatPassProvider seatPassProvider = new SeatPassFileReader();
        LockerPassProvider lockerPassProvider = new LockerPassFilerReader();

        StudyCafePassMachine studyCafePassMachine = new StudyCafePassMachine(seatPassProvider, lockerPassProvider);
        studyCafePassMachine.run();
    }

}
