package com.readable.code.minesweeper.cell;

public abstract class Cell {
	// abstract 클래스는 인스턴스를 만들 수 없다.

	protected static final String FLAG_SIGN = "⚑";
	protected static final String UNCHECKED_SIGN = "□";

	protected boolean isLandMine;
	protected boolean isFlagged;
	protected boolean isOpened;

	/** 생성자 보다는, 정적 팩토리 메소드를 즐겨 사용한다. */
	public abstract boolean isLandMine ();

	public abstract boolean hasLandMineCount ();

	public abstract String getSign ();

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

}
