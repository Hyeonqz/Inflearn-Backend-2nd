package com.readable.code.minesweeper.io;

import com.readable.code.minesweeper.board.position.CellPosition;
import com.readable.code.minesweeper.user.UserAction;

public interface InputHandler {
	UserAction getUserActionFromUser();
	CellPosition getCellPositionFromUser();
}
