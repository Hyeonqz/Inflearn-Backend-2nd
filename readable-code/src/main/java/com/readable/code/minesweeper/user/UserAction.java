package com.readable.code.minesweeper.user;

public enum UserAction {
	OPEN("Cell Open"),
	FLAG("Flag put in."),
	UNKNOWN("I don t know")
	;

	private String description;

	UserAction (String description) {
		this.description = description;
	}
}
