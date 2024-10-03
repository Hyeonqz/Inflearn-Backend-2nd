package com.readable.code.minesweeper.gameLevel;

public class VeryBeginner implements GameLevel{

	@Override
	public int getRowSize () {
		return 4;
	}

	@Override
	public int getColSize () {
		return 5;
	}

	@Override
	public int getLandMineCount () {
		return 2;
	}

}
