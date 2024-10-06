package com.readable.code.minesweeper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.readable.code.minesweeper.cell.Cell;
import com.readable.code.minesweeper.cell.CellState;
import com.readable.code.minesweeper.cell.Cells;
import com.readable.code.minesweeper.cell.EmptySell;
import com.readable.code.minesweeper.cell.LandMineCell;
import com.readable.code.minesweeper.cell.NumberCell;
import com.readable.code.minesweeper.gameLevel.GameLevel;
import com.readable.code.minesweeper.position.CellPosition;
import com.readable.code.minesweeper.position.CellPositions;
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
		Cells cells = Cells.from(board);
		return cells.isAllChecked();
	}

	public void initializeGame() {
		CellPositions cellPositions = CellPositions.from(board);

		initializeEmptyCells(cellPositions);

		List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
		initializeLandMineCells(landMinePositions);

		List<CellPosition> numberPositionsCandidates =x cellPositions.subtract(landMinePositions);
		initializeNumberCells(numberPositionsCandidates);
	}

	private void initializeEmptyCells (CellPositions cellPositions) {
		List<CellPosition> allPositions = cellPositions.getPositions();
		for(CellPosition cellPosition : allPositions) {
			updateCellAt(cellPosition, new EmptySell());
		}
	}

	private void initializeLandMineCells (List<CellPosition> landMinePositions) {
		for(CellPosition cellPosition : landMinePositions) {
			updateCellAt(cellPosition, new LandMineCell());
		}
	}

	private void initializeNumberCells (List<CellPosition> numberPositionsCandidates) {
		for(CellPosition candidatePositions : numberPositionsCandidates) {
			int count = countNearbyLandMines(candidatePositions);

			if(count != 0)
				updateCellsAt(numberPositionsCandidates, new NumberCell(count));

		}
	}

	private void updateCellsAt (List<CellPosition> allPositions, Cell cell) {
		for(CellPosition cellPosition : allPositions) {
			updateCellAt(cellPosition, cell);
		}
	}

	private void updateCellAt (CellPosition position, Cell cell) {
		board[position.getRowIndex()][position.getColIndex()] = cell;
	}

	public String getSign(CellPosition cellPosition) {
		Cell cell = findCell(cellPosition);
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

		long count = calculatedSurroundedPositions(cellPosition,rowSize,colSize).stream()
			.filter(this::isLandMineCellAt)
			.count();

		return (int)count;

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