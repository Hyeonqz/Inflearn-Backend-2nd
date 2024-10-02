package com.readable.code.minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

	public static final Scanner SCANNER = new Scanner(System.in);
	private static final int BOARD_ROW_SIZE = 8;
	private static final int BOARD_COLUMN_SIZE = 10;
	private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
	private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];

	private static int gameStatus = 0; // 0:게임중, 1:승리, -1:패배

	public static void main(String[] args) {
		showGameStartComments();
		initializeGame();

		while (true) {
			try {
				showBoard();

				if (doesUserWinTheGame()) {
					System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
					break;
				}
				if (doesUserLoseTheGame()) {
					System.out.println("지뢰를 밟았습니다. GAME OVER!");
					break;
				}

				String cellInput = getCellInputFromUser();
				String userActionInput = getUserActionFromUser();

				actOnCell(cellInput, userActionInput);
			} catch (AppException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) { // 우리가 예상치 못한 예외가 생길 때 처리
				System.out.println("프로그램에 문제가 생겼다");
				// e.printStackTrace(); // 실무에서는 안티패턴이다, 보통 로그를 통해서 에러 해결한다
			}

		}
	}

	private static void actOnCell (String cellInput, String userActionInput) {
		int selectedColIndex = getSelectedColIndex(cellInput);
		int selectedRowIndex = getSelectedRowIndex(cellInput);

		if (doesUserChooseToPlantFlag(userActionInput)) {
			BOARD[selectedRowIndex][selectedColIndex].flag();
			checkIfGameIsOver();
			return;
		}

		if (doesUserChooseToOpenCell(userActionInput)) {
			if (landMineCell(selectedRowIndex, selectedColIndex)) {
				BOARD[selectedRowIndex][selectedColIndex].open();
				changeGameStatusToLose();
				return;
			}
			open(selectedRowIndex, selectedColIndex);
			checkIfGameIsOver();
			return;
		}
		System.out.println("잘못된 번호를 선택했습니다.");
	}

	private static void changeGameStatusToLose () {
		gameStatus = -1;
	}

	private static boolean landMineCell (int selectedRowIndex, int selectedColIndex) {
		return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
	}

	private static boolean doesUserChooseToOpenCell (String userActionInput) {
		return userActionInput.equals("1");
	}

	private static boolean doesUserChooseToPlantFlag (String userActionInput) {
		return userActionInput.equals("2");
	}

	private static int getSelectedRowIndex (String cellInput) {
		char cellInputRow = cellInput.charAt(1);
		return convertRowFrom(cellInputRow);
	}

	private static int getSelectedColIndex (String cellInput) {
		char cellInputCol = cellInput.charAt(0);
		return convertColFrom(cellInputCol);
	}

	private static String getUserActionFromUser () {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
		return SCANNER.nextLine();
	}

	private static String getCellInputFromUser () {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
		return SCANNER.nextLine();
	}

	private static boolean doesUserWinTheGame () {
		return gameStatus == 1;
	}

	private static boolean doesUserLoseTheGame() {
		return gameStatus == -1;
	}

	private static void checkIfGameIsOver() {
		boolean isAllChecked = isAllCellChecked();
		if (isAllChecked) {
			changeGameStatusToWin();
		}
	}

	private static void changeGameStatusToWin () {
		gameStatus = 1;
	}

	private static boolean isAllCellChecked() {
		return Arrays.stream(BOARD)
			.flatMap(Arrays::stream)
			.allMatch(Cell::isChecked);
	}

	private static int convertRowFrom(char cellInputRow) {
		int rowIndex = Character.getNumericValue(cellInputRow)-1;
		if(rowIndex > BOARD_ROW_SIZE)
			throw new AppException("잘못된 입력 입니다");
		return rowIndex;
	}

	private static int convertColFrom(char cellInputCol) {
		switch (cellInputCol) {
			case 'a':
				return 0;
			case 'b':
				return 1;
			case 'c':
				return 2;
			case 'd':
				return 3;
			case 'e':
				return 4;
			case 'f':
				return 5;
			case 'g':
				return 6;
			case 'h':
				return 7;
			case 'i':
				return 8;
			case 'j':
				return 9;
			default:
				throw new AppException("잘못된 값이 입력되었습니다");
		}
	}

	private static void showBoard() {
		System.out.println("   a b c d e f g h i j");
		for (int row = 0; row < 8; row++) {
			System.out.printf("%d  ", row + 1);
			for (int col = 0; col < 10; col++) {
				System.out.print(BOARD[row][col].getSign() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void initializeGame() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 10; col++) {
				BOARD[row][col] = Cell.create();
			}
		}

		for (int i = 0; i < 10; i++) {
			int col = new Random().nextInt(10);
			int row = new Random().nextInt(8);
			BOARD[row][col].turnOnLandMine();
		}

		for (int row = 0; row < BOARD_ROW_SIZE; row++) {
			for (int col = 0; col < BOARD_COLUMN_SIZE; col++) {
				int count = 0;
				if (!landMineCell(row, col)) {
					if (row - 1 >= 0 && col - 1 >= 0 && landMineCell(row - 1, col - 1)) {
						count++;
					}
					if (row - 1 >= 0 && landMineCell(row - 1, col)) {
						count++;
					}
					if (row - 1 >= 0 && col + 1 < 10 && landMineCell(row - 1, col + 1)) {
						count++;
					}
					if (col - 1 >= 0 && landMineCell(row, col - 1)) {
						count++;
					}
					if (col + 1 < 10 && landMineCell(row, col + 1)) {
						count++;
					}
					if (row + 1 < 8 && col - 1 >= 0 && landMineCell(row + 1, col - 1)) {
						count++;
					}
					if (row + 1 < 8 && landMineCell(row + 1, col)) {
						count++;
					}
					if (row + 1 < 8 && col + 1 < 10 && landMineCell(row + 1, col + 1)) {
						count++;
					}
					NEARBY_LAND_MINE_COUNTS[row][col] = count;
				}
				BOARD[row][col].updateNearbyLandMineCount(count);
			}
		}
	}

	private static void showGameStartComments() {
		System.out.println("-----------------------------------");
		System.out.println("지뢰찾기 게임 시작!");
		System.out.println("-----------------------------------");
	}

	private static void open(int row, int col) {
		if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COLUMN_SIZE) {
			return;
		}
		if (BOARD[row][col].isOpened()) {
			return;
		}
		if (landMineCell(row, col)) {
			return;
		}

		BOARD[row][col].open();

		if (BOARD[row][col].hasLandMineCount())  {
			return;
		}

		open(row - 1, col - 1);
		open(row - 1, col);
		open(row - 1, col + 1);
		open(row, col - 1);
		open(row, col + 1);
		open(row + 1, col - 1);
		open(row + 1, col);
		open(row + 1, col + 1);
	}

}