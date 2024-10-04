package com.readable.code.minesweeper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.readable.code.minesweeper.cell.Cell;
import com.readable.code.minesweeper.cell.CellState;
import com.readable.code.minesweeper.cell.EmptySell;
import com.readable.code.minesweeper.cell.LandMineCell;
import com.readable.code.minesweeper.cell.NumberCell;
import com.readable.code.minesweeper.gameLevel.GameLevel;
import com.readable.code.minesweeper.position.CellPosition;
import com.readable.code.minesweeper.position.RelativePosition;

public class GameBoard {
	private final Cell[][] board;
	private final int landMineCount;

	public GameBoard (GameLevel gameLevel) {
		int colSize = gameLevel.getColSize();
		int rowSize = gameLevel.getRowSize();
		board = new Cell[rowSize][colSize];

		landMineCount = gameLevel.getLandMineCount();
	}

	public void flagAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.flag();
	}

	public void openAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		cell.open();
	}

	private Cell findCell(CellPosition cellPosition) {
		return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
	}

	public void openSurroundedCells(CellPosition cellPosition) {
		if ( cellPosition.isRowIndexMoreThanOrEqual(getRowSize())
			|| cellPosition.isColIndexMoreThanOrEqual(getColSize())) {
			return;
		}

		if (isOpenedCell(cellPosition)) {
			return;
		}

		if (isLandMineCellAt(cellPosition)) {
			return;
		}

		openAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(getRowSize()))
			.forEach(this::openSurroundedCells);
	}

	private boolean canMovePosition (CellPosition cellPosition, RelativePosition relativePosition) {
		return cellPosition.canCalculatePositionBy(relativePosition);
	}

	private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
		return findCell(cellPosition).hasLandMineCount();
	}

	public boolean isInvalidCellPosition(CellPosition cellPosition) {
		int colSize = getColSize();
		int rowSize = getRowSize();
		return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColIndexMoreThanOrEqual(colSize);
	}

	private boolean isOpenedCell(CellPosition cellPosition) {
		return findCell(cellPosition).isOpened();
	}

	public boolean isLandMineCellAt(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
		return cell.isLandMine();
	}

	public boolean isAllCellChecked() {
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.allMatch(Cell::isChecked);
	}

	public void initializeGame() {
		int rowSize = getRowSize();
		int colSize = getColSize();

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				board[row][col] = new EmptySell();
			}
		}

		for (int i = 0; i < landMineCount; i++) {
			int landMineCol = new Random().nextInt(colSize);
			int landMineRow = new Random().nextInt(rowSize);

			board[landMineRow][landMineCol] = new LandMineCell();
		}

		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				if (isLandMineCell(row, col)) {
					continue;
				}

				int count = countNearbyLandMines(row, col);
				if(count == 0)
					continue;

				board[row][col] = new NumberCell(count);
			}
		}
	}

	public String getSign(int rowIndex, int colIndex) {
		Cell cell = findCell(rowIndex, colIndex);
		return cell.getSign();
	}



	public int getRowSize() {
		return board.length;
	}

	public int getColSize() {
		return board[0].length;
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

}