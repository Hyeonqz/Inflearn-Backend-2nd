package com.readable.code.minesweeper.cell;

public class CellState {

	private boolean isFlagged;
	private boolean isOpened;

	public CellState (boolean isFlagged, boolean isOpened) {
		this.isFlagged = isFlagged;
		this.isOpened = isOpened;
	}

	public static CellState initialize() {
		return new CellState(false, false);
	}

	public void flag () {
		this.isFlagged = true;
	}

	public boolean isChecked () {
		return isFlagged || isOpened;
	}

	public void open () {
		this.isOpened = true;
	}

	public boolean isOpened () {
		return isOpened;
	}

	public boolean isFlagged () {
		return isFlagged;
	}

}