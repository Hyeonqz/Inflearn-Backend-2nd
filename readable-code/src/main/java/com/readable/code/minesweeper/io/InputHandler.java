package com.readable.code.minesweeper.io;

import com.readable.code.minesweeper.position.CellPosition;

public interface InputHandler {
	String getUserInput();
	CellPosition getCellPositionFromUser();
}
