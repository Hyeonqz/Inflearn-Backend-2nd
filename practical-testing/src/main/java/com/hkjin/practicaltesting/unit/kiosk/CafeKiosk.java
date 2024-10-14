package com.hkjin.practicaltesting.unit.kiosk;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hkjin.practicaltesting.unit.beverate.Beverage;
import com.hkjin.practicaltesting.unit.order.Order;

import lombok.Getter;

@Getter
public class CafeKiosk {
	private final List<Beverage> beverages = new ArrayList<>();

	public void add (Beverage beverage) {
		beverages.add(beverage);
	}

	public void add(Beverage beverage, int count) {
		if(count<=0)
			throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");

		for (int i = 0; i < count; i++) {
			beverages.add(beverage);
		}
	}

	// 부분 삭제
	public void delete(Beverage beverage) {
		beverages.remove(beverage);
	}

	// 전체 삭제
	public void clear() {
		beverages.clear();
	}

	public int calculateTotalPrice () {
		int totalPrice = 0;
		for (Beverage beverage : beverages) {
			totalPrice += beverage.getPrice();
		}
		return totalPrice;
	}

	public Order createOrder() {
		return new Order(LocalDateTime.now(), beverages);
	}

}
