package com.readable.code.minesweeper.io;

import java.util.List;
import java.util.stream.IntStream;

import com.readable.code.minesweeper.GameBoard;
import com.readable.code.minesweeper.GameException;
import com.readable.code.minesweeper.cell.CellSnapshot;
import com.readable.code.minesweeper.cell.CellSnapshotStatus;
import com.readable.code.minesweeper.position.CellPosition;

public class ConsoleOutputHandler implements OutputHandler{
	protected static final String EMPTY_SIGN = "◼";
	private static final String LAND_MINE_SIGN = "*";
	private static final String FLAG_SIGN = "⚑";
	private static final String UNCHECKED_SIGN = "□";

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
				CellPosition cellPosition = CellPosition.of(row, col);

				CellSnapshot cellSnapshot = board.getSnapshot(cellPosition);
				String cellSign = decideCellSignFrom(cellSnapshot);

				System.out.print(cellSign + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private String decideCellSignFrom (CellSnapshot cellSnapshot) {
		CellSnapshotStatus status = cellSnapshot.getStatus();
		if(status == CellSnapshotStatus.EMPTY)
			return EMPTY_SIGN;
		if(status == CellSnapshotStatus.FLAG)
			return FLAG_SIGN;
		if(status == CellSnapshotStatus.LAND_MINE)
			return LAND_MINE_SIGN;
		if(status == CellSnapshotStatus.NUMBER)
			return String.valueOf(cellSnapshot.getNearbyLandMineCount());
		if(status == CellSnapshotStatus.UNCHECKED)
			return UNCHECKED_SIGN;

		throw new IllegalArgumentException("확인할 수 없는 셀 입니다");
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
