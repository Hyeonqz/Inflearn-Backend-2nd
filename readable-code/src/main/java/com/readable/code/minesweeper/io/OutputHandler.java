package com.readable.code.minesweeper.io;

import com.readable.code.minesweeper.game.GameBoard;
import com.readable.code.minesweeper.exception.GameException;

public interface OutputHandler {
	void showGameStartComments ();

	void showBoard (GameBoard board);

	void showGameWinningComment ();

	void showGameLoseComment ();

	void showCommentForSelectingCell ();

	void showCommentForUserAction ();

	void showExceptionMessage (GameException e);

	void showSimpleMessage ();

}