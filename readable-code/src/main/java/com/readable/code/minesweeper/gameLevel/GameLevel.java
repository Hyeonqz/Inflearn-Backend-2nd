package com.readable.code.minesweeper.gameLevel;

public interface GameLevel {
	// 추상화를 다이렉트로 표현한 구조가 인터페이스 이다.
	// 인터페이스가 가지는 스펙들이 메소드 선언부를 선언한다.
	int getRowSize();
	int getColSize();
	int getLandMineCount();

	// 마인스위퍼 입장에서는 게임 레벨 인터페이스를 받긴한다.
	// 하지만 실제로 뭐가들어올지 모른다, 런타임 시점에 어떤 구현체가 들어올지 결정을 한다.
}
