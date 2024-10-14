package com.readable.code.minesweeper.board.position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.readable.code.minesweeper.board.cell.Cell;

public class CellPositions {
	// CellPosition 에 대한 1급 컬렉션
	private final List<CellPosition> positions;

	public CellPositions (List<CellPosition> positions) {
		this.positions = positions;
	}

	public static CellPositions of(List<CellPosition> positions) {
		return new CellPositions(positions);
	}

	public static CellPositions from (Cell[][] board) {
		List<CellPosition> cellPositions = new ArrayList<>();

		for(int row=0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				CellPosition cellPosition = CellPosition.of(row,col);
				cellPositions.add(cellPosition);
			}
		}
		return null;
	}

	public List<CellPosition> extractRandomPositions (int count) {
		ArrayList<CellPosition> cellPositions = new ArrayList<>(positions);
		Collections.shuffle(cellPositions);
		return cellPositions.subList(0,count);
	}

	public List<CellPosition> getPositions () {
		return new ArrayList<>(positions);
	}

	public List<CellPosition> subtract (List<CellPosition> positionListToSubtract) {
		List<CellPosition> cellPositions = new ArrayList<>(this.positions);

		CellPositions positionsToSubtract = CellPositions.of(positionListToSubtract);

		return cellPositions.stream()
			.filter(positionsToSubtract::doesNotContain)
			.toList();
	}

	private boolean doesNotContain (CellPosition position) {
		return !positions.contains(position);
	}

}
