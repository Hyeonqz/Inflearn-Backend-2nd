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
		if (isOpenedCell(cellPosition))
			return;

		if (isLandMineCellAt(cellPosition))
			return;

		openAt(cellPosition);

		if (doesCellHaveLandMineCount(cellPosition)) {
			return;
		}

		List<CellPosition> surroundedPositions = calculatedSurroundedPositions(cellPosition,getRowSize(),getColSize());
		surroundedPositions.forEach(this::openSurroundedCells);
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
				CellPosition cellPosition = CellPosition.of(row, col);
				if (isLandMineCell(cellPosition)) {
					continue;
				}

				int count = countNearbyLandMines(cellPosition);
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

	private int countNearbyLandMines(CellPosition cellPosition) {
		int rowSize = getRowSize();
		int colSize = getColSize();

		List<CellPosition> count = calculatedSurroundedPositions(cellPosition,rowSize,colSize).stream()
			.filter(this::isLandMineCellAt)
			.toList();

		return

	}

	private List<CellPosition> calculatedSurroundedPositions (CellPosition cellPosition, int rowSize, int colSize) {
		return RelativePosition.SURROUNDED_POSITIONS.stream()
			.filter(cellPosition::canCalculatePositionBy)
			.map(cellPosition::calculatePositionBy)
			.filter(position -> position.isRowIndexLessThan(rowSize))
			.filter(position -> position.isColIndexLessThan(colSize))
			.toList();
	}

}