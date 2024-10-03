package com.readable.code.minesweeper;

public class BoardIndexConverter {

	private static final char BASE_CHAR_FOR_COL = 'a';

	public int getSelectedColIndex (String cellInput, int colSize) {
		char cellInputCol = cellInput.charAt(0);
		return convertColFrom(cellInputCol, colSize);
	}

	public int getSelectedRowIndex (String cellInput, int rowSize) {
		String cellInputRow = cellInput.substring(1);
		return convertRowFrom(cellInputRow, rowSize);
	}


	private int convertColFrom(char cellInputCol, int colSize) {
		int colIndex = cellInputCol - BASE_CHAR_FOR_COL; // 소문자 계산

		if(colIndex < 0 || colIndex >= colSize)
			throw new GameException("You wrong enter");

		return colIndex;
	}

	private int convertRowFrom(String cellInputRow, int rowSize) {
		int rowIndex = Integer.parseInt(cellInputRow) -1;
		if (rowIndex < 0 || rowIndex >= rowSize)
			throw new GameException("잘못된 입력입니다.");

		return rowIndex;
	}

}

