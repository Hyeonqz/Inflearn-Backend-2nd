package com.readable.code.minesweeper.io.minesweeper;

import com.readable.code.game.GameRunnable;
import com.readable.code.minesweeper.config.GameConfig;
import com.readable.code.minesweeper.exception.GameException;
import com.readable.code.minesweeper.io.BoardIndexConverter;
import com.readable.code.minesweeper.board.GameBoard;
import com.readable.code.minesweeper.board.GameStatus;
import com.readable.code.minesweeper.gameLevel.GameLevel;
import com.readable.code.minesweeper.io.InputHandler;
import com.readable.code.minesweeper.io.OutputHandler;
import com.readable.code.minesweeper.board.position.CellPosition;
import com.readable.code.minesweeper.user.UserAction;

public class Minesweeper implements GameRunnable {

	private final GameBoard gameBoard;
	private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
	private final InputHandler inputHandler;
	private final OutputHandler outputHandler;
	private GameStatus gameStatus; // 0: 게임 중, 1: 승리, -1: 패배

	public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
		gameBoard = new GameBoard(gameLevel);
		this.inputHandler = inputHandler;
		this.outputHandler = outputHandler;
	}

	public Minesweeper(GameConfig gameConfig) {
		gameBoard = new GameBoard(gameConfig.getGameLevel());
		this.inputHandler = gameConfig.getInputHandler();
		this.outputHandler = gameConfig.getOutputHandler();
		gameStatus = GameStatus.IN_PROGRESS;
	}

	@Override
	public void run() {
		outputHandler.showGameStartComments();

		while (gameStatus==GameStatus.IN_PROGRESS) {
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
				UserAction userActionInput = getUserActionInputFromUser();
				actOnCell(cellInput, userActionInput);
			} catch (GameException e) {
				outputHandler.showExceptionMessage(e);
			} catch (Exception e) {
				outputHandler.showSimpleMessage();
			}
		}
		outputHandler.showBoard(gameBoard);
	}

	private void actOnCell(CellPosition cellPosition, UserAction userActionInput) {
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
		gameStatus = GameStatus.LOSE;
	}

	private boolean doesUserChooseToOpenCell(UserAction userActionInput) {
		return userActionInput == UserAction.OPEN;
	}

	private boolean doesUserChooseToPlantFlag(UserAction userActionInput) {
		return userActionInput == UserAction.FLAG;
	}

	private UserAction getUserActionInputFromUser() {
		outputHandler.showCommentForUserAction();
		return inputHandler.getUserActionFromUser();
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
		return gameStatus == GameStatus.LOSE;
	}

	private boolean doesUserWinTheGame() {
		return gameStatus == GameStatus.WIN;
	}

	private void checkIfGameIsOver() {
		if (gameBoard.isAllCellChecked()) {
			changeGameStatusToWin();
		}
	}

	private void changeGameStatusToWin() {
		gameStatus = GameStatus.WIN;
	}

}