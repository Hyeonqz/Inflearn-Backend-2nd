package com.readable.code.minesweeper;

import com.readable.code.minesweeper.game.GameInitializable;
import com.readable.code.minesweeper.game.GameRunnable;
import com.readable.code.minesweeper.gameLevel.GameLevel;
import com.readable.code.minesweeper.io.ConsoleInputHandler;
import com.readable.code.minesweeper.io.ConsoleOutputHandler;
import com.readable.code.minesweeper.io.InputHandler;
import com.readable.code.minesweeper.io.OutputHandler;
import com.readable.code.minesweeper.position.CellPosition;

public class Minesweeper implements GameInitializable, GameRunnable {

	private final GameBoard gameBoard;
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

	public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
		gameBoard = new GameBoard(gameLevel);
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	@Override
	public void initialize () {
		gameBoard.initializeGame();
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (true) {
			try {
				outputHandler.showBoard(gameBoard);

				if (doesUserWinTheGame()) {
					outputHandler.showGameWinningComment();
					break;
				}
				if (doesUserLoseTheGame()) {
					outputHandler.showGameLoseComment();
					break;
				}

				CellPosition cellInput = getCellInputFromUser();
				String userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage();
			}
		}
	}

	private void actOnCell(CellPosition cellPosition, String userActionInput) {
		if (doesUserChooseToPlantFlag(userActionInput)) {
			gameBoard.flagAt(cellPosition);
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (gameBoard.isLandMineCellAt(cellPosition)) {
				gameBoard.openAt(cellPosition);
				changeGameStatusToLose();
				return;
			}

			gameBoard.openSurroundedCells(cellPosition);
			checkIfGameIsOver();
			return;
		}
		throw new GameException("잘못된 번호를 선택하셨습니다.");
	}

	private void changeGameStatusToLose() {
		gameStatus = -1;
	}

	private boolean doesUserChooseToOpenCell(String userActionInput) {
		return userActionInput.equals("1");
	}

	private boolean doesUserChooseToPlantFlag(String userActionInput) {
		return userActionInput.equals("2");
	}

	private String getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserInput();
	}

	private CellPosition getCellInputFromUser() {
		outputHandler.showCommentForSelectingCell();
		CellPosition cellPosition = inputHandler.getCellPositionFromUser();

		if(gameBoard.isInvalidCellPosition(cellPosition)) {
			throw new GameException("잘못된 좌표를 선택하셨습니다.");
		}

		return inputHandler.getCellPositionFromUser();
	}

	private boolean doesUserLoseTheGame() {
		return gameStatus == -1;
	}

	private boolean doesUserWinTheGame() {
		return gameStatus == 1;
	}

	private void checkIfGameIsOver() {
		if (gameBoard.isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = 1;
	}

}