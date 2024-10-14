package com.readable.code.minesweeper.game;

public enum GameStatus {
	IN_PROGRESS("진행중"),
	WIN("승리"),
	LOSE("패배")
	;

	private String description;

	GameStatus (String description) {
		this.description = description;
	}
}
