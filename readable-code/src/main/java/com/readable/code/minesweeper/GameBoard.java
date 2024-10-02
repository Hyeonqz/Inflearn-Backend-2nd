package com.readable.code.minesweeper;

import java.util.Random;

public class GameBoard {
	private static final int LAND_MINE_COUNT = 10;

	private final Cell[][] board;

	public GameBoard (int row, int col) {
		board = new Cell[row][col];
	}

	public int getRowSize () {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
	}

	public String getSign (int row, int col) {
		Cell cell = findCell(row, col);
		return cell.getSign();
	}

	private Cell findCell (int row, int col) {
		return board[row][col];
	}

	public void initializeGame() {
		int rowSize = board.length;
		int colSize = board[0].length;

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				BOARD[row][col] = Cell.create();
			}
		}

		for (int i = 0; i < LAND_MINE_COUNT; i++) {
			int landMineCol = new Random().nextInt(colSize);
			int landMineRow = new Random().nextInt(rowSize);
			findCell(landMineRow, landMineCol).turnOnLandMine();
		}

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if(isLandMinceCell(row,col)) {
					continue;
				}
				int count = countNearbyLandMines(row,col);
				findCell(row, col).updateNearbyLandMineCount(count);

			}
		}
	}

	private int countNearbyLandMines(int row, int col) {
		int rowSize = getRowSize();
		int colSize = getColSize();
		int count = 0;
		if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
			count++;
		}
		if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
			count++;
		}
		if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
			count++;
		}
		if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
			count++;
		}
		if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
			count++;
		}
		if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
			count++;
		}
		if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
			count++;
		}
		if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
			count++;
		}
		return count;
	}

	public boolean isLandMinceCell (int selectedRowIndex, int selectedColIndex) {
		return findCell(selectedRowIndex, selectedColIndex).isLandMine();
	}

	public void flag (int selectedRowIndex, int selectedColIndex) {
		findCell(selectedRowIndex,selectedColIndex).flag();
	}

}
