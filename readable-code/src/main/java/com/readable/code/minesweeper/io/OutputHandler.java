package com.readable.code.minesweeper.io;

import java.util.List;
import java.util.stream.IntStream;

import com.readable.code.minesweeper.GameBoard;
import com.readable.code.minesweeper.GameException;

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