package com.readable.code.minesweeper;

public class Cell {

	private static final String FLAG_SIGN = "⚑";
	private static final String LAND_MINE_SIGN = "*";
	private static final String CLOSED_CELL_SIGN = "□";
	private static final String UNCHECKED_SIGN = "□";
	private static final String OPENED_CELL_SIGN = "◼️";
	private static final String EMPTY_SIGN = "◼️";

	private int nearbyLandMineCount;
	private boolean isLandMine;
	private boolean isFlagged;
	private boolean isOpened;

	private Cell (int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
		this.nearbyLandMineCount = nearbyLandMineCount;
		this.isLandMine = isLandMine;
		this.isFlagged = isFlagged;
		this.isOpened = isOpened;
	}

	// Cell 이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
	// Cell 의 상태? : 깃발 유무, open/closed, 사용자가 확인함.

	// 생성자 보다는, 정적 팩토리 메소드를 즐겨 사용한다.
	public static Cell of(int nearbyLandMineCount, boolean isLandMine) {
		return new Cell(nearbyLandMineCount , isLandMine, false, false);
	}

	public static Cell create () {
		return of(0,false);
	}

	public void turnOnLandMine () {
		this.isLandMine = true;
	}

	public void updateNearbyLandMineCount (int count) {
		this.nearbyLandMineCount = count;
	}

	// 1. Getter/Setter 는 처음부터 만들지 말자.

	public String getSign () {
		if(isOpened) {
			if(isLandMine) {
				return LAND_MINE_SIGN;
			}
			if(hasLandMineCount()) {
				return String.valueOf(nearbyLandMineCount);
			}
			return EMPTY_SIGN;
		}

		if(isFlagged) {
			return FLAG_SIGN;
		}

		return UNCHECKED_SIGN;
	}

	public void flag () {
		this.isFlagged = true;
	}

	public boolean isChecked () {
		return isFlagged || isOpened;
	}

	public boolean isLandMine () {
		return this.isLandMine;
	}

	public void open () {
		this.isOpened = true;
	}

	public boolean isOpened () {
		return isOpened;
	}

	public boolean hasLandMineCount () {
		return this.nearbyLandMineCount != 0;
	}

}
