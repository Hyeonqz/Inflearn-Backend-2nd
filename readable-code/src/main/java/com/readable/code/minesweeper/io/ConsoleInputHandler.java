package com.readable.code.minesweeper.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.readable.code.minesweeper.game.BoardIndexConverter;
import com.readable.code.minesweeper.position.CellPosition;
import com.readable.code.minesweeper.user.UserAction;

public class ConsoleInputHandler implements InputHandler{

	public static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();

	@Override
	public UserAction getUserActionFromUser () {
		try {
			String userInput = br.readLine();

			if("1".equals(userInput)) {
				return UserAction.OPEN;
			}
			if("2".equals(userInput)) {
				return UserAction.FLAG;
			}
			return UserAction.UNKNOWN;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CellPosition getCellPositionFromUser () {
		try {
			String userInput = br.readLine();
			int colIndex = boardIndexConverter.getSelectedColIndex(userInput);
			int rowIndex = boardIndexConverter.getSelectedRowIndex(userInput);

			return CellPosition.of(rowIndex, colIndex);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
