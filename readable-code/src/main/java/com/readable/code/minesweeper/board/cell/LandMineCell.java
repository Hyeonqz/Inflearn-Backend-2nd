package com.readable.code.minesweeper.board.cell;

public class LandMineCell implements Cell {
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
	public CellSnapshot getSnapshot () {
		if(cellState.isOpened())
			return CellSnapshot.ofLandmine();

		if(cellState.isFlagged())
			return CellSnapshot.ofFlag();

		return CellSnapshot.ofUnchecked();
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
