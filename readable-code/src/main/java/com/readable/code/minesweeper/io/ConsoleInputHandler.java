package com.readable.code.minesweeper.io;

import java.util.Scanner;

public class ConsoleInputHandler {

	public static final Scanner SCANNER = new Scanner(System.in);

	public String getUserInput () {
		return SCANNER.nextLine();
	}



}
