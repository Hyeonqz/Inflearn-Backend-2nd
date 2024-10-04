package com.readable.code.minesweeper.io;

import java.util.List;
import java.util.stream.IntStream;

import com.readable.code.minesweeper.GameBoard;
import com.readable.code.minesweeper.GameException;

public class ConsoleOutputHandler implements OutputHandler{
	@Override
	public void showGameStartComments() {
		System.out.println("-----------------------------------");
		System.out.println("지뢰찾기 게임 시작!");
		System.out.println("-----------------------------------");
	}

	@Override
	public void showBoard(GameBoard board) {
		String alphabets = generateColAlphabets(board);
		System.out.println("    " + alphabets);

		for (int row = 0; row < board.getRowSize(); row++) {
			System.out.printf("%2d  ", row + 1);
			for (int col = 0; col < board.getColSize(); col++) {
				System.out.print(board.getSign(row,col) + " ");
			}
			System.out.println();
		}

		System.out.println();
	}

	private String generateColAlphabets (GameBoard board) {
		List<String> list = IntStream.range(0, board.getColSize())
			.mapToObj(idx -> (char)('a' + idx))
			.map(Object::toString)
			.toList();

		return String.join(" ", list);
	}

	@Override
	public void showGameWinningComment () {
		System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
	}

	@Override
	public void showGameLoseComment() {
		System.out.println("지뢰를 밟았습니다. GAME OVER!");
	}

	@Override
	public void showCommentForSelectingCell () {
		System.out.println("선택할 좌표를 입력하세요. (예: a1)");
	}

	@Override
	public void showCommentForUserAction () {
		System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
	}

	@Override
	public void showExceptionMessage (GameException e) {
		System.out.println(e.getMessage());
	}

	@Override
	public void showSimpleMessage () {
		System.out.println("프로그램에 문제가 생겼다");
	}

}
