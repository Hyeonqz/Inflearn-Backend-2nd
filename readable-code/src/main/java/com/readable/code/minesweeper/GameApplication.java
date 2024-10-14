package com.readable.code.minesweeper;

import com.readable.code.minesweeper.config.GameConfig;
import com.readable.code.minesweeper.gameLevel.Advanced;
import com.readable.code.minesweeper.gameLevel.GameLevel;
import com.readable.code.minesweeper.io.ConsoleInputHandler;
import com.readable.code.minesweeper.io.ConsoleOutputHandler;
import com.readable.code.minesweeper.io.InputHandler;
import com.readable.code.minesweeper.io.OutputHandler;
import com.readable.code.minesweeper.io.minesweeper.Minesweeper;

public class GameApplication {

	public static void main (String[] args) {
		GameLevel gameLevel = new Advanced();
		InputHandler inputHandler = new ConsoleInputHandler();
		OutputHandler outputHandler = new ConsoleOutputHandler();

		GameConfig gameConfig = new GameConfig(
			gameLevel, inputHandler, outputHandler
		);

		Minesweeper mineSweeper = new Minesweeper(gameConfig);
		//mineSweeper.initialize();
		mineSweeper.run();
	}

	/*
	* DIP (Dependency Inversion Principle)

	* Spring 3대원칙 -  IoC, DI, AOP
	* IoC(제어의 역전) : 어플리케이션 흐름을 프레임워크에게 맡긴다, 빈에 등록된 객체를 생성해주고, 생명주기를 관리 해준다.
	* 	- 생성 해주고, 소멸도 해주고, DI 를 통한 의존성 주입도 해주고, 개발자는 쓰기만 하면된다.
	* 	- 스프링 컨테이너, IoC 컨테이너 라고도 한다.

	* DI(의존성 주입) : bean 등록 -> 의존성 등록 -> 외부에서 의존성을 주입을 받는다. 즉 등록해둔 bean 을 대입받는다.
	* 	- "3" 숫자 가 떠올라야 한다. 제 3자가 A,B 의 의존성을 맺어줘야한다. 스프링 컨텍스트가 이 역할을 한다(bean)
	*
	*/
}