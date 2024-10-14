package com.readable.code.minesweeper.board.cell;

import java.util.Arrays;
import java.util.List;

public class Cells {
	// 1급 컬렉션 -> 필드가 1개다

	private final List<Cell> cells;

	public Cells (List<Cell> cells) {
		this.cells = cells;
	}

	public static Cells of(List<Cell> cells) {
		return new Cells(cells);
	}

	public static Cells from(Cell[][] cells) {
		List<Cell> list = Arrays.stream(cells)
			.flatMap(Arrays::stream)
			.toList();
		return Cells.of(list);
	}

	public boolean isAllChecked() {
		return cells.stream()
			.allMatch(Cell::isChecked);
	}

}
