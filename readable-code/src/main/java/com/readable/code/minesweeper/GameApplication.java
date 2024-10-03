package com.readable.code.minesweeper;

import com.readable.code.minesweeper.gameLevel.Advanced;
import com.readable.code.minesweeper.gameLevel.Beginner;
import com.readable.code.minesweeper.gameLevel.GameLevel;

public class GameApplication {

	public static void main (String[] args) {
		GameLevel gameLevel = new Advanced();

		Minesweeper mineSweeper = new Minesweeper(gameLevel);
		mineSweeper.run();
	}
}