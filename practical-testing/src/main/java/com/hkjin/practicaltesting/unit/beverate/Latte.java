package com.hkjin.practicaltesting.unit.beverate;

public class Latte implements Beverage {
	@Override
	public int getPrice () {
		return 5000;
	}

	@Override
	public String getName () {
		return "라뗴";
	}

}
