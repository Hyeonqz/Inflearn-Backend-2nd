package com.readable.code.minesweeper.board.position;

import java.util.Objects;

public class CellPosition {
	// 불변성(final), 동등성, 유효성
	private final int rowIndex;
	private final int colIndex;

	public CellPosition (int rowIndex, int colIndex) {
		if(rowIndex < 0 || colIndex < 0)
			throw new IllegalArgumentException("올바르지 않은 좌표 입니다.");

		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public static CellPosition of(int rowIndex, int colIndex) {
		return new CellPosition(rowIndex, colIndex);
	}

	@Override
	public boolean equals (Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CellPosition that = (CellPosition)o;
		return rowIndex == that.rowIndex && colIndex == that.colIndex;
	}

	@Override
	public int hashCode () {
		return Objects.hash(rowIndex, colIndex);
	}

	public boolean isRowIndexMoreThanOrEqual (int rowSize) {
		return this.rowIndex >= rowSize;
	}

	public boolean isColIndexMoreThanOrEqual (int rowSize) {
		return this.colIndex >= rowSize;
	}

	public int getRowIndex () {
		return this.rowIndex;
	}

	public int getColIndex () {
		return this.colIndex;
	}

	public CellPosition calculatePositionBy (RelativePosition relativePosition) {
		if(this.canCalculatePositionBy(relativePosition)) {
			return CellPosition.of(
				this.rowIndex + relativePosition.getDeltaRow(),
				this.colIndex + relativePosition.getDeltaCol()
			);
		}
		throw new IllegalArgumentException("움직일 수 있는 좌표가 아닙니다.");
	}

	public boolean canCalculatePositionBy (RelativePosition relativePosition) {
		return this.rowIndex + relativePosition.getDeltaRow() >= 0
			&& this.colIndex + relativePosition.getDeltaCol() >= 0;
	}

	public boolean isRowIndexLessThan (int rowSize) {
		return this.rowIndex < rowSize;
	}

	public boolean isColIndexLessThan(int colIndex) {
		return this.colIndex < colIndex;
	}


}
