package com.hkjin.practicaltesting.unit.beverate;

public class Americano implements Beverage{
	@Override
	public int getPrice () {
		return 4500;
	}

	@Override
	public String getName () {
		return "아메리카노";
	}

}
