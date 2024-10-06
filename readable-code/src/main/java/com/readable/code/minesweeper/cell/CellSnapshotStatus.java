package com.readable.code.minesweeper.cell;

public enum CellSnapshotStatus {
	EMPTY("빈 셀"),
	FLAG("깃발"),
	LAND_MINE("지뢰"),
	NUMBER("숫자"),
	UNCHECKED("확인전")
	;

	private final String description;

	CellSnapshotStatus (final String description) {
		this.description = description;
	}
}
