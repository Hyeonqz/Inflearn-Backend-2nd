package com.readable.code.minesweeper.cell;

public class EmptySell extends Cell {
	protected static final String EMPTY_SIGN = "◼";

	@Override
	public boolean isLandMine () {
		return false;
	}

	@Override
	public boolean hasLandMineCount () {
		return false;
	}

	@Override
	public String getSign () {
		if(isOpened) {
			return EMPTY_SIGN;
		}
		if(isFlagged) {
			return FLAG_SIGN;
		}
		return UNCHECKED_SIGN;
	}
}
