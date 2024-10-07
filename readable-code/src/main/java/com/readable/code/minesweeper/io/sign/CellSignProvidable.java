package com.readable.code.minesweeper.io.sign;

import com.readable.code.minesweeper.cell.CellSnapshot;

// 메소드의 스펙을 가지고 있어야 한다.
public interface CellSignProvidable {
	String provide(CellSnapshot cellSnapshot);
	boolean supports(CellSnapshot cellSnapshot);
}
