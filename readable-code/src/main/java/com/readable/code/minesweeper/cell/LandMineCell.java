package com.readable.code.minesweeper.cell;

public class LandMineCell implements Cell {
	private static final String LAND_MINE_SIGN = "*";
	private final CellState cellState = CellState.initialize();

	private boolean isLaneMine;

	@Override
	public boolean isLandMine () {
		return true;
	}

	@Override
	public boolean hasLandMineCount () {
		return false;
	}

	@Override
	public String getSign () {
		if(cellState.isOpened()) {
			return LAND_MINE_SIGN;
		}
		if(cellState.isFlagged()) {
			return FLAG_SIGN;
		}
		return UNCHECKED_SIGN;
	}

	@Override
	public void flag () {
		cellState.flag();
	}

	@Override
	public void open () {
		cellState.open();
	}

	@Override
	public boolean isChecked () {
		return cellState.isChecked();
	}

	@Override
	public boolean isOpened () {
		return cellState.isOpened();
	}
}
